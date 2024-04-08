package backend;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RestController
@CrossOrigin(origins = App.CORS_ORIGIN, allowCredentials = "true")
public class LoginRestController {
    
    @Autowired
    private UsernameDatabase usernameDatabase;

    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int MAX_REGISTER_ATTEMPTS = 3;
    public static final String LOGIN_COOLDOWN = "1m";
    public static final String REGISTER_COOLDOWN = "1m";

    private LoadingCache<String, Integer> registerAttemptsCache;
    private LoadingCache<String, Integer> loginAttemptsCache;

    public LoginRestController() {
        registerAttemptsCache = CacheBuilder.from(CacheBuilderSpec.parse("maximumSize=1000,expireAfterWrite="+REGISTER_COOLDOWN))
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) {
                    return 0;
                }
            });
        loginAttemptsCache = CacheBuilder.from(CacheBuilderSpec.parse("maximumSize=1000,expireAfterWrite="+LOGIN_COOLDOWN))
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) {
                    return 0;
                }
            });
    }

    @PostMapping("/api/login")
    public Map<String, String> login(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        boolean isRateLimited = isRateLimited(req, MAX_LOGIN_ATTEMPTS, loginAttemptsCache);
        if (isRateLimited) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Too many attempts. Try again later.");
        }

        // Existing user in the system
        SiteUser user = null;
        try {
            user = usernameDatabase.select(UsernameDatabase.Category.EMAIL, email);
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Failed to log in.");
        }

        if (user == null) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // User not registered
            return Map.of("message", "Failed to log in.");
        }

        if (!SiteUser.checkPassword(password, user.getEncryptedPassword())) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // Passwords do not match. Implement a delay to prevent brute force attacks.
            return Map.of("message", "Failed to log in.");
        }

        String token = generateToken(email);
        usernameDatabase.loginUser(token, user);

        Cookie tokenCookie = new Cookie("_authHH", token);
        res.addCookie(tokenCookie);

        return Map.of("message", "Login successful.", "token", token);
    }

    @DeleteMapping("/api/logout")
    public Map<String, String> logout(@CookieValue(value = "_auth", defaultValue = "") String token, HttpServletResponse res) {
        if (token.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Failed to log out (Invalid token).");
        }
        usernameDatabase.logoutUser(token);
        return Map.of("message", "Logout successful.");
    }

    @PostMapping("/api/register")
    public Map<String, String> register(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> body) {
        // TODO: Implement a rate limiter to prevent brute force attacks
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");
        String recaptcha = body.get("captcha");
        String remoteAddress = req.getRemoteAddr();

        if (isRateLimited(req, MAX_REGISTER_ATTEMPTS, registerAttemptsCache)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Too many attempts. Try again later.");
        }

        // Check if the reCAPTCHA is valid
        boolean isCaptchaValid = Captcha.verifyCaptcha(recaptcha, remoteAddress);
        isCaptchaValid = true; // TODO: Remove this line
        if (!isCaptchaValid) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Invalid reCAPTCHA.");
        }

        // See if someone has the same username
        SiteUser usernameMatch = null;
        try {
            usernameMatch = usernameDatabase.select(UsernameDatabase.Category.USERNAME, username);
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Failed to register.");
        }

        if (usernameMatch != null) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Username taken.");
        }

        // See if someone has the same email
        SiteUser emailMatch = null;
        try {
            emailMatch = usernameDatabase.select(UsernameDatabase.Category.EMAIL, email);
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Failed to register.");
        }
        if (emailMatch != null) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Account already registered under email.");
        }

        try {
            usernameDatabase.register(username, email, password);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Map.of("message", "Failed to register.");
        }

        // Rate limit the user so they can't spam registration
        registerAttemptsCache.asMap().remove(getRemoteIP(req));
        registerAttemptsCache.put(getRemoteIP(req), MAX_REGISTER_ATTEMPTS);

        return Map.of("message", "Registration successful.");   
    }

    private static String SECRET = null;
    private static final long EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes
    
    public static String getJwtSecret() {
        // Read from file ".config/jwt_secret.csv"

        if (SECRET != null) {
            return SECRET;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(".config/jwt_secret.csv"));
            String secret = reader.readLine();
            reader.close();
            SECRET = secret;
            return secret;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, getJwtSecret())
            .compact();
    }
    public static String extractUsername(String token) {
        return Jwts.parser()
            .setSigningKey(getJwtSecret())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(getJwtSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getRemoteIP(HttpServletRequest req) {
        String remoteAddress = req.getHeader("X-Forwarded-For");
        if (remoteAddress == null) {
            remoteAddress = req.getRemoteAddr();
        }
        return remoteAddress;
    }

    private boolean isRateLimited(HttpServletRequest req, int maxAttempts, LoadingCache<String, Integer> cache) {
        String remoteIP = getRemoteIP(req);
        Integer requests = 0;
        try {
            requests = cache.get(remoteIP);
            if (requests != null) {
                if (requests >= maxAttempts) {
                    cache.asMap().remove(remoteIP);
                    cache.put(remoteIP, requests);
                    return true;
                } else {
                    cache.put(getRemoteIP(req), requests + 1);
                }
            } else {
                requests = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        requests++;
        cache.put(remoteIP, requests);
        return false;
    }
}

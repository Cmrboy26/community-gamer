package backend;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoginRestController {
    
    UsernameDatabase usernameDatabase;

    public LoginRestController(UsernameDatabase usernameDatabase) {
        this.usernameDatabase = usernameDatabase;
    }

    @PostMapping("/api/login")
    public Map<String, String> login(HttpServletResponse res, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String encryptedPassword = SiteUser.encrypt(password);

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

        if (!Objects.equals(user.getEncryptedPassword(), encryptedPassword)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // Passwords do not match. Implement a delay to prevent brute force attacks.
            return Map.of("message", "Failed to log in.");
        }

        String token = generateToken(email);
        usernameDatabase.loginUser(token, user);
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
    public Map<String, String> register(HttpServletResponse res, @RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

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
            return Map.of("message", "Failed to register.");
        }

        usernameDatabase.register(username, email, password);
        return Map.of("message", "Registration successful.");   
    }

    private static final String SECRET = "e8866f08812ef052e39a309ff9cd19f24e5fcec5b73f2670f78128d0027415a4"; // TODO: make a new one and dont have it public lol
    private static final long EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes
    public static String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }
    public static String extractUsername(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}

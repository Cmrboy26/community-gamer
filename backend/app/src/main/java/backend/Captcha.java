package backend;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Captcha {

    public static boolean verifyCaptcha(String captcha, String remoteip) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "https://www.google.com/recaptcha/api/siteverify";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("secret", getReCAPTCHASecret()) // TODO: make this secret key not public
            .queryParam("remoteip", remoteip)
            .queryParam("response", captcha);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
            builder.toUriString(),
            org.springframework.http.HttpMethod.POST,
            entity,
            String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode success = root.path("success");
            return success.asBoolean();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String secretCache = null;

    public static String getReCAPTCHASecret() {

        // Read the secret key from file ".config/recaptcha_secret.csv"
        // getReCAPTCHASecret: 6LcR77IpAAAAAHSbELDQ5fcsBL4FqM5-_Zf9Xbr3
        if (secretCache != null) {
            return secretCache;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(".config/recaptcha_secret.csv"));
            String secret = reader.readLine();
            reader.close();
            secretCache = secret;
            return secret;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    
}

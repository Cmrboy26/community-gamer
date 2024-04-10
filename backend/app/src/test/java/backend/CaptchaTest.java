package backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CaptchaTest {
    @Test void captchaKeyPresent() {
        assertNotNull(Captcha.getReCAPTCHASecret(), "Recaptcha secret key is not present in .config/recaptcha_secret.csv");
    }
    @Test void captchaKeyValid() {
        // TODO: Add a test to verify that the secret key is valid
    }
}
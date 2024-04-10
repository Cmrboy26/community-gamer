package backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class LoginTest {
    @Test void isJWTKeyPresent() {
        assertNotNull(LoginRestController.getJwtSecret(), "JWT secret key is not present in .config/jwt_secret.csv");
    }
}

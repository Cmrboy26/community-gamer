package backend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class AppTest {
    
    @Test void configFolderExistsAndIsFolder() {
        File file = new File(".config/");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    @Test void configFolderContainsDatabaseCSV() {
        File file = new File(".config/database.csv");
        assertTrue(file.exists(), ".config/database.csv, containing the database connection information, is missing.");
    }

    @Test void configFolderContainsJWTSecretKey() {
        File file = new File(".config/jwt_secret.csv");
        assertTrue(file.exists(), ".config/jwt_secret.csv, containing the JWT secret key, is missing.");
    }

    @Test void configFolderContainsReCAPTCHASecretKey() {
        File file = new File(".config/recaptcha_secret.csv");
        assertTrue(file.exists(), ".config/recaptcha_secret.csv, containing the reCAPTCHA secret key, is missing.");
    }

}

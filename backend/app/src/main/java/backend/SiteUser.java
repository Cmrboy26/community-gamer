package backend;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SiteUser {
    
    private String username;
    private String email;
    private String encryptedpassword;

    public SiteUser(String username, String email, String encryptedpassword) {
        this.username = username;
        this.email = email;
        this.encryptedpassword = encryptedpassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptedPassword() {
        return encryptedpassword;
    }

    public static SiteUser from(String username, String email, String password, boolean passwordIsEncrypted) {
        if (passwordIsEncrypted) {
            return new SiteUser(username, email, password);
        }
        return new SiteUser(username, email, encrypt(password));
    }

    public static final int HASH_STRENGTH = 12;

    public static String encrypt(String unencryptedPassword) {
        // TODO: Implement encryption.
        String encryptedHash = BCrypt.withDefaults().hashToString(HASH_STRENGTH, unencryptedPassword.toCharArray());
        return encryptedHash;
    }

    public static boolean checkPassword(String unencryptedPassword, String encryptedPassword) {
        return BCrypt.verifyer().verify(unencryptedPassword.toCharArray(), encryptedPassword).verified;
    }

    @Override
    public String toString() {
        return "SiteUser [email=" + email + ", encryptedpassword=" + encryptedpassword + ", username=" + username + "]";
    }
}

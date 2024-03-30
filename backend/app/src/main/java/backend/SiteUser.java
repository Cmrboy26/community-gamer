package backend;

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

    public static String encrypt(String unencryptedPassword) {
        // TODO: Implement encryption.
        return unencryptedPassword+"ENCRYPTED";
    }
}

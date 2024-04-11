package backend;

import com.google.common.primitives.UnsignedInteger;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SiteUser {
    
    private long id;
    private String username;
    private String email;
    private String encryptedpassword;

    public SiteUser(UnsignedInteger id, String username, String email, String encryptedpassword) {
        this.id = id.longValue();
        this.username = username;
        this.email = email;
        this.encryptedpassword = encryptedpassword;
    }

    public UnsignedInteger getId() {
        return UnsignedInteger.valueOf(id);
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

    public static SiteUser from(UnsignedInteger id, String username, String email, String password, boolean passwordIsEncrypted) {
        if (passwordIsEncrypted) {
            return new SiteUser(id, username, email, password);
        }
        return new SiteUser(id, username, email, encrypt(password));
    }

    public static final int HASH_STRENGTH = 12;

    public static String encrypt(String unencryptedPassword) {
        String encryptedHash = BCrypt.withDefaults().hashToString(HASH_STRENGTH, unencryptedPassword.toCharArray());
        return encryptedHash;
    }

    public static boolean checkPassword(String unencryptedPassword, String encryptedPassword) {
        return BCrypt.verifyer().verify(unencryptedPassword.toCharArray(), encryptedPassword).verified;
    }

    @Override
    public String toString() {
        return "SiteUser [id="+id+", email=" + email + ", encryptedpassword=" + encryptedpassword + ", username=" + username + "]";
    }
}

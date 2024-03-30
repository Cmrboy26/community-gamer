package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Objects;

public class UsernameDatabase {

    public HashMap<String, SiteUser> tokenToUser;
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/users";
    private static final String DATABASE_USERNAME = "username";
    private static final String DATABASE_PASSWORD = "password";

    public UsernameDatabase() {
        tokenToUser = new HashMap<>();
    }

    public void logoutUser(String token) {
        tokenToUser.remove(token);
    }
    public void logoutUser(SiteUser user) {
        tokenToUser.values().remove(user);
    }

    public void loginUser(String token, SiteUser user) {
        tokenToUser.put(token, user);
    }

    public SiteUser getUser(String token) {
        return tokenToUser.get(token);
    }

    public enum Category {
        USERNAME,
        EMAIL,
        PASSWORD
    }

    public SiteUser select(Category c1, String v1) throws SQLException {
        Objects.requireNonNull(c1);
        Objects.requireNonNull(v1);
        return select(c1, selectQuery(c1, v1));
    }

    public SiteUser select(Category c1, String v1, Category c2, String v2) throws SQLException {
        Objects.requireNonNull(c1);
        Objects.requireNonNull(c2);
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        return select(c1, selectQuery(c1, v1), selectQuery(c2, v2));
    }

    public SiteUser select(Category prefered, String...selectQueries) throws SQLException {
        String query = "SELECT * FROM users WHERE ";
        int amount = selectQueries.length;
        int i = 0;
        for (String selectQuery : selectQueries) {
            query += selectQuery;
            if (i < amount - 1) {
                query += " AND ";
            }
            i++;
        }

        ResultSet resultSet = executeQuery(query);
        if (resultSet.next()) {
            String username = resultSet.getString(Category.USERNAME.name().toLowerCase());
            String email = resultSet.getString(Category.EMAIL.name().toLowerCase());
            String password = resultSet.getString(Category.PASSWORD.name().toLowerCase());
            return SiteUser.from(username, email, password, true);
        }
        return null;
    }

    public void register(String username, String email, String unencryptedPassword) {
        // TODO: Implement. Throw exception if user already exists.
    }

    // returns "c = 'v'"
    private String selectQuery(Category c, String v) {
        return c.name().toLowerCase() + " = '" + v + "'";
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

}
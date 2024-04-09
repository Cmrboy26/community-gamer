package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.google.common.primitives.UnsignedInteger;

@Component
public class UsernameDatabase {

    // TODO: use a loading cache to store the token to user mapping
    public HashMap<String, SiteUser> tokenToUser;

    public UsernameDatabase() {
        tokenToUser = new HashMap<>();

        // Get the database url inside "database.csv"
        

        // Test get for username 'Cmrboy26'
        try {
            SiteUser user = select(Category.USERNAME, "Cmrboy26");
            System.out.println(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public boolean isUserLoggedIn(String token) {
        if (token == null) {
            return false;
        }
        if (token.isEmpty()) {
            return false;
        }
        if (!tokenToUser.containsKey(token)) {
            return false;
        }
        boolean validToken = LoginRestController.isTokenValid(token);
        if (!validToken) {
            tokenToUser.remove(token);
        }
        return validToken;
    }

    public enum Category {
        ID,
        USERNAME,
        EMAIL,
        PASSWORD
    }

    public SiteUser select(Category c1, String v1) throws SQLException {
        Objects.requireNonNull(c1);
        Objects.requireNonNull(v1);
        return selectUser(c1, selectQuery(c1, v1));
    }

    public SiteUser select(Category c1, String v1, Category c2, String v2) throws SQLException {
        Objects.requireNonNull(c1);
        Objects.requireNonNull(c2);
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        return selectUser(c1, selectQuery(c1, v1), selectQuery(c2, v2));
    }

    public SiteUser selectUser(Category prefered, String...selectQueries) throws SQLException {
        String query = "SELECT * FROM users WHERE ";
        query += String.join(" AND ", selectQueries);
        query += ";";
        Connection connection = createConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            UnsignedInteger id = UnsignedInteger.valueOf(resultSet.getLong(Category.ID.name().toLowerCase()));
            String username = resultSet.getString(Category.USERNAME.name().toLowerCase());
            String email = resultSet.getString(Category.EMAIL.name().toLowerCase());
            String password = resultSet.getString(Category.PASSWORD.name().toLowerCase());
            connection.close();
            return SiteUser.from(id, username, email, password, true);
        }
        connection.close();
        return null;
    }

    public void register(String username, String email, String unencryptedPassword) throws SQLException {
        String encryptedPassword = SiteUser.encrypt(unencryptedPassword);

        Connection connection = createConnection();
        PreparedStatement query = connection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
        query.setString(1, username);
        query.setString(2, email);
        query.setString(3, encryptedPassword);

        query.executeUpdate();
        connection.close();
    }

    // returns "c = 'v'"
    private String selectQuery(Category c, String v) {
        return c.name().toLowerCase() + " = '"+v+"'";
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    private Connection createConnection() throws SQLException {
        return SQLDatabase.createConnection();
    }
}

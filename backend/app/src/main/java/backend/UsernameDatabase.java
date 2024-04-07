package backend;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.mysql.cj.PreparedQuery;

@Component
public class UsernameDatabase {

    public HashMap<String, SiteUser> tokenToUser;
    public final String DATABASE_URL; // = "jdbc:mysql://localhost:3306/users";
    private final String DATABASE_USERNAME; // = "username";
    private final String DATABASE_PASSWORD; // = "password";

    public UsernameDatabase() {
        tokenToUser = new HashMap<>();

        // Get the database url inside "database.csv"
        String[] database;
        try {
            File file = new File(".config/database.csv");
            InputStream inputStream = file.toURI().toURL().openStream();
            database = new String(inputStream.readAllBytes()).split(",");
        } catch (Exception e) {
            e.printStackTrace();
            database = new String[3];
            database[0] = "jdbc:mysql://localhost:3306/users";
            database[1] = "username";
            database[2] = "password";
        }
        if (database.length != 3) {
            throw new IllegalArgumentException("database.csv must contain 3 values separated by commas.");
        }
        DATABASE_URL = database[0];
        DATABASE_USERNAME = database[1];
        DATABASE_PASSWORD = database[2];

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

    public enum Category {
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
        int amount = selectQueries.length;
        int i = 0;
        for (String selectQuery : selectQueries) {
            query += selectQuery;
            if (i < amount - 1) {
                query += " AND ";
            }
            i++;
        }
        System.out.println(query);

        ResultSet resultSet = executeQuery(query);
        if (resultSet.next()) {
            String username = resultSet.getString(Category.USERNAME.name().toLowerCase());
            String email = resultSet.getString(Category.EMAIL.name().toLowerCase());
            String password = resultSet.getString(Category.PASSWORD.name().toLowerCase());
            return SiteUser.from(username, email, password, true);
        }
        return null;
    }

    public void register(String username, String email, String unencryptedPassword) throws SQLException {
        String encryptedPassword = SiteUser.encrypt(unencryptedPassword);

        String url = "jdbc:mysql://"+DATABASE_URL + "/sql5695331";
        Connection connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
        PreparedStatement query = connection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
        query.setString(1, username);
        query.setString(2, email);
        query.setString(3, encryptedPassword);

        query.executeUpdate();
        connection.close();
    }

    // returns "c = 'v'"
    private String selectQuery(Category c, String v) {
        return c.name().toLowerCase() + " = '" + v + "'";
    }

    private ResultSet executeQuery(String query) throws SQLException {
        String url = "jdbc:mysql://"+DATABASE_URL + "/sql5695331";
        Connection connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

}

package backend;

import java.io.File;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabase {
    
    private static String DATABASE_URL = null;
    private static String DATABASE_USERNAME = null;
    private static String DATABASE_PASSWORD = null;
    private static String DATABASE_NAME = null;

    private static void loadValues() {
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
            database[3] = "databasename";
        }
        if (database.length != 3) {
            throw new IllegalArgumentException("database.csv must contain 3 values separated by commas.");
        }
        DATABASE_URL = database[0];
        DATABASE_USERNAME = database[1];
        DATABASE_PASSWORD = database[2];
        DATABASE_NAME = database[3];
    }

    public static String getDatabaseURL() {
        if (DATABASE_URL == null) {
            loadValues();
        }
        return DATABASE_URL;
    }

    public static String getDatabaseUsername() {
        if (DATABASE_USERNAME == null) {
            loadValues();
        }
        return DATABASE_USERNAME;
    }

    public static String getDatabasePassword() {
        if (DATABASE_PASSWORD == null) {
            loadValues();
        }
        return DATABASE_PASSWORD;
    }

    public static String getDatabaseName() {
        if (DATABASE_NAME == null) {
            loadValues();
        }
        return DATABASE_NAME;
    }

    public static Connection createConnection() throws SQLException {
       // String url = "jdbc:mysql://"+getDatabaseURL() + "/sql5695331";
        String url = "jdbc:mysql://"+getDatabaseURL() + "/" + getDatabaseName();
        Connection connection = DriverManager.getConnection(url, getDatabaseUsername(), getDatabasePassword());
        return connection;
    }

}

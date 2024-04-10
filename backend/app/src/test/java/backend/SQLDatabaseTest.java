package backend;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

public class SQLDatabaseTest {
    
    @Test void sqlDatabaseIsValidAndAccessable() {
        // If no exception is thrown, the test is successful
        try {
            Connection connection = SQLDatabase.createConnection();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("The credentials in \"database.csv\" are invalid. Please check the file and try again.");
        }
    }

}

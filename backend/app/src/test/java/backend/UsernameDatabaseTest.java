package backend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

public class UsernameDatabaseTest {
    
    @Test void sqlUsernameDatabaseIsAccessable() throws Exception {
        Connection connection = SQLDatabase.createConnection();
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES");
        
        boolean blogTableExists = false;
        while (set.next()) {
            String tableName = set.getString("TABLE_NAME");
            if (tableName.equals("users")) {
                blogTableExists = true;
                break;
            }
        } 
        System.out.flush();

        connection.close();
        assertTrue(blogTableExists);
    }

    @Test void sqlUsernameDatabaseHasCorrectColumns() throws Exception {
        Connection connection = SQLDatabase.createConnection();
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'users'");
        
        boolean idColumnExists = false;
        boolean usernameColumnExists = false;
        boolean emailColumnExists = false;
        boolean passwordColumnExists = false;

        boolean idIsInt = false;
        boolean usernameIsText = false;
        boolean emailIsText = false;
        boolean passwordIsText = false;

        while (set.next()) {
            String datatype = set.getString("DATA_TYPE");
            String columnName = set.getString("COLUMN_NAME");
            if (columnName.equals("id")) {
                idColumnExists = true;
                if (datatype.contains("int")) {
                    idIsInt = true;
                }
            } else if (columnName.equals("username")) {
                usernameColumnExists = true;
                if (datatype.contains("text")) {
                    usernameIsText = true;
                }
            } else if (columnName.equals("email")) {
                emailColumnExists = true;
                if (datatype.contains("text")) {
                    emailIsText = true;
                }
            } else if (columnName.equals("password")) {
                passwordColumnExists = true;
                if (datatype.contains("text")) {
                    passwordIsText = true;
                }
            }
        }

        connection.close();
        assertTrue(idColumnExists, "id column does not exist");
        assertTrue(usernameColumnExists, "username column does not exist");
        assertTrue(emailColumnExists, "email column does not exist");
        assertTrue(passwordColumnExists, "password column does not exist");

        assertTrue(idIsInt, "id column is not of type INT");
        assertTrue(usernameIsText, "username column is not of type TEXT");
        assertTrue(emailIsText, "email column is not of type TEXT");
        assertTrue(passwordIsText, "password column is not of type TEXT");
    }

}

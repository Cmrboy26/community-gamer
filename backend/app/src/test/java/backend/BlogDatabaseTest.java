package backend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

public class BlogDatabaseTest {

    @Test void sqlBlogDatabaseIsAccessable() throws Exception {
        Connection connection = SQLDatabase.createConnection();
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES");
        
        boolean blogTableExists = false;
        while (set.next()) {
            String tableName = set.getString("TABLE_NAME");
            if (tableName.equals("blogs")) {
                blogTableExists = true;
                break;
            }
        } 
        System.out.flush();

        connection.close();
        assertTrue(blogTableExists);
    }

    @Test void sqlBlogDatabaseHasCorrectColumns() throws Exception {
        Connection connection = SQLDatabase.createConnection();
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'blogs'");
        
        boolean idColumnExists = false;
        boolean useridColumnExists = false;
        boolean dateColumnExists = false;
        boolean jsonColumnExists = false;

        boolean idIsInt = false;
        boolean useridIsInt = false;
        boolean dateIsDateTime = false;
        boolean jsonIsText = false;
        while (set.next()) {
            String datatype = set.getString("DATA_TYPE");
            String columnName = set.getString("COLUMN_NAME");
            if (columnName.equals("id")) {
                idColumnExists = true;
                if (datatype.contains("int")) {
                    idIsInt = true;
                }
            } else if (columnName.equals("userid")) {
                useridColumnExists = true;
                if (datatype.contains("int")) {
                    useridIsInt = true;
                }
            } else if (columnName.equals("date")) {
                dateColumnExists = true;
                if (datatype.contains("datetime")) {
                    dateIsDateTime = true;
                }
            } else if (columnName.equals("json")) {
                jsonColumnExists = true;
                if (datatype.contains("text")) {
                    jsonIsText = true;
                }
            }
        }

        connection.close();
        assertTrue(idColumnExists, "id column does not exist");
        assertTrue(useridColumnExists, "userid column does not exist");
        assertTrue(dateColumnExists, "date column does not exist");
        assertTrue(jsonColumnExists, "json column does not exist");

        assertTrue(idIsInt, "id column is not of type INT");
        assertTrue(useridIsInt, "userid column is not of type INT");
        assertTrue(dateIsDateTime, "date column is not of type DATETIME");
        assertTrue(jsonIsText, "json column is not of type TEXT");
    }
}

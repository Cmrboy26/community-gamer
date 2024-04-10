package backend;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

public class BlogDatabaseText {

    @Test void sqlBlogDatabaseIsAccessbale() throws Exception {
        Connection connection = SQLDatabase.createConnection();
        connection.close();
    }
}

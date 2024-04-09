package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Component;

import backend.Blog.BlogSection;

@Component
public class BlogDatabase {

    public BlogDatabase() {

    }

    protected void load() {

    }
    
    public static void save(BlogDatabase database) {

    }

    public String getBlogString(long id) {
        try {
            Connection connection = SQLDatabase.createConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM blogs WHERE id = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                connection.close();
                return set.getString("json");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Blog getBlog(long id) {
        Connection connection = null;
        try {
            connection = SQLDatabase.createConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM blogs WHERE id = ?");
            statement.setLong(1, id);

            ResultSet set = statement.executeQuery();
            Blog blog = new Blog(set);
            connection.close();
            return blog;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("Blog not found.");
    }

    public void addBlog(Blog blog) {

    }

    public void deleteBlog(long id) {

    }

}

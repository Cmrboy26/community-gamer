package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BlogDatabase {

    public BlogDatabase() {

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

    public List<Blog> getBlogs(int maxBlogs, String serachTerm) {
        String query = "SELECT * FROM blogs ";
        if (serachTerm != null) {
            query += "WHERE json LIKE ? ";
        }
        query += "ORDER BY date DESC LIMIT ?";
        ArrayList<Blog> blogs = new ArrayList<Blog>();
        try {
            Connection connection = SQLDatabase.createConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            if (serachTerm != null) {
                statement.setString(1, "%"+serachTerm.toString()+"%");
                statement.setInt(2, maxBlogs);
            } else {
                statement.setInt(1, maxBlogs);
            }
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Blog blog = new Blog(set, true);
                blogs.add(blog);
            }
            connection.close();
            return blogs;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBlog(SiteUser user, Blog blog) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // I don't want ID and user id to be serialized in this mapper.
            mapper.addMixIn(Blog.class, Blog.IDExemptMixin.class);
            String json = mapper.writeValueAsString(blog);

            Connection connection = SQLDatabase.createConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO blogs (userid, date, json) VALUES (?, ?, ?)");
            statement.setLong(1, user.getId().longValue());
            statement.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            statement.setString(3, json);
            statement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBlog(SiteUser user, long id) {
        // TODO: Verify that this functions correctly.
        try {
            Connection connection = SQLDatabase.createConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM blogs WHERE id = ? AND userid = ?");
            statement.setLong(1, id);
            statement.setLong(2, user.getId().longValue());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

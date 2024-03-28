package backend;

import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class BlogDatabase {
    
    private HashMap<Long, Blog> blogs;

    public BlogDatabase() {
        blogs = new HashMap<Long, Blog>();
        load();
    }

    protected void load() {
        long random = new Random().nextLong(1000);
        addBlog(new Blog(random, "Title"+random, "Body"+random+" here is ze body"));
    }
    public static void save(BlogDatabase database) {
        // Save the database to a file
        System.out.println("Saving database...");
    }

    public Blog getBlog(long id) {
        Blog result = blogs.get(id);
        if (result == null) {
            throw new IllegalArgumentException("Blog not found");
        }
        return result;
    }

    public void addBlog(Blog blog) {
        blogs.put(blog.getId(), blog);
    }

    public void deleteBlog(long id) {
        blogs.remove(id);
    }

    public HashMap<Long, Blog> getBlogs() {
        return blogs;
    }

}

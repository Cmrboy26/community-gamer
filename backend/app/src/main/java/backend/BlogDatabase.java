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
        for (int i = 0; i < 100; i++) {
            //addBlog(new Blog(random+i, "Title"+random, "Body"+random+" here is ze body "+i*4));
        }
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
        long id = findNextFreeBlogID();
        blogs.put(id, blog);
    }

    public long findNextFreeBlogID() {
        return blogs.size();
    }

    public void deleteBlog(long id) {
        blogs.remove(id);
    }

    public HashMap<Long, Blog> getBlogs() {
        return blogs;
    }

}

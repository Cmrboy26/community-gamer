package backend;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import backend.Blog.BlogSection;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = App.CORS_ORIGIN, allowCredentials = "true")
public class BlogRestController {
    
    @Autowired
    private BlogDatabase blogDatabase;
    @Autowired
    private UsernameDatabase usernameDatabase;

    @GetMapping("/api/blog/{id}")
    public Blog getBlog(@PathVariable long id) {
        try {
            return blogDatabase.getBlog(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/blogs")
    public Collection<Blog> getBlogs() {
        return blogDatabase.getBlogs().values();
    }

    @PostMapping("/api/postblog")
    public String postBlog(HttpServletRequest request, @CookieValue(value = "_auth", defaultValue = "") String token, @RequestBody Blog blog) {
        if (!usernameDatabase.isUserLoggedIn(token)) {
            return "User is not logged in.";
        }
        blogDatabase.addBlog(blog);
        return "This is a temporary response. ";
    }

}

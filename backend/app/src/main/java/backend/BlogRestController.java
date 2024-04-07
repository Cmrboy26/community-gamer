package backend;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = App.CORS_ORIGIN)
public class BlogRestController {
    
    @Autowired
    private BlogDatabase blogDatabase;

    /*@GetMapping("/api/blog/{id}")
    public Blog getBlog(@PathVariable long id) {
        return blogDatabase.getBlog(id);
    }*/

    @GetMapping("/api/blog/{id}")
    public Blog getBlog(@PathVariable long id) {
        try {
            // return blogDatabase.getBlog(id);
            return Blog.builder()
                .ID(id)
                .title("Title"+id)
                .category("Category"+id)
                .tags("HACKS!", "CHEATER")
                
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/blogs")
    public Collection<Blog> getBlogs() {
        return blogDatabase.getBlogs().values();
    }

    @GetMapping("/api/postblog")
    public String temporaryPostBlog(@CookieValue(value = "_auth", defaultValue = "") String token) {
        if (token.isEmpty()) {
            return "You are not logged in.";
        }
        return "This is a temporary response. ";
    }

}

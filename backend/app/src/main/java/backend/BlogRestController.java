package backend;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogRestController {
    
    @Autowired
    private BlogDatabase blogDatabase;

    @GetMapping("/api/blog/{id}")
    public Blog getBlog(@PathVariable long id) {
        return blogDatabase.getBlog(id);
    }

    @GetMapping("/api/blogs")
    public Collection<Blog> getBlogs() {
        return blogDatabase.getBlogs().values();
    }

    @GetMapping("/api/postblog")
    public String temporaryPostBlog(Authentication authentication) {
        return "This is a temporary response. "+authentication.getName();
    }

}

package backend;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogRestController {
    
    @Autowired
    private BlogDatabase blogDatabase;

    @GetMapping("/blog/{id}")
    public Blog getBlog(@PathVariable long id) {
        return blogDatabase.getBlog(id);
    }

    @GetMapping("/blogs")
    public Collection<Blog> getBlogs() {
        return blogDatabase.getBlogs().values();
    }

    @PostMapping("/blog")
    public Blog postBlog(@RequestBody Blog blog) {
        return blog;
    }

}

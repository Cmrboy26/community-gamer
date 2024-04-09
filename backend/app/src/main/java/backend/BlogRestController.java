package backend;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = App.CORS_ORIGIN, allowCredentials = "true")
public class BlogRestController {
    
    @Autowired
    private BlogDatabase blogDatabase;
    @Autowired
    private UsernameDatabase usernameDatabase;

    private static final String POST_COOLDOWN = "1m";
    private LoadingCache<String, Integer> postAttemptsCache;

    public BlogRestController() {
        postAttemptsCache = CacheBuilder.from(CacheBuilderSpec.parse("maximumSize=1000,expireAfterWrite="+POST_COOLDOWN)).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(@Nonnull String key) {
                return 0;
            }
        });
    }

    @GetMapping("/api/blog/{id}")
    public Blog getBlog(@PathVariable long id) {
        try {
            return blogDatabase.getBlog(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/api/blogs")
    public Collection<Blog> getBlogs(@RequestBody String query) {
        List<Blog> blogs = blogDatabase.getBlogs(100, query);
        return blogs;
    }

    @PostMapping("/api/postblog")
    public String postBlog(HttpServletRequest request, @CookieValue(value = "_auth", defaultValue = "") String token, @RequestBody Blog blog) {
        boolean rateLimited = LoginRestController.isRateLimited(request, 5, postAttemptsCache);
        if (rateLimited) {
            return "Rate limited.";
        }

        if (!usernameDatabase.isUserLoggedIn(token)) {
            return "User is not logged in.";
        }
        SiteUser user = usernameDatabase.getUser(token);
        blogDatabase.addBlog(user, blog);
        return "Posted successfully.";
    }

}

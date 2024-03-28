package backend;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class App {
    
    BlogDatabase database;
    BlogRestController blogRestController;

    public App() {
        database = new BlogDatabase();
        blogRestController = new BlogRestController(); 
    }

    @PreDestroy
    public void close() {
        BlogDatabase.save(database);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    }

    public static void closeSpringApp(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }
}

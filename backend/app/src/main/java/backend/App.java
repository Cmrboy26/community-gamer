package backend;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

    public static void closeSpringApp(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }
}

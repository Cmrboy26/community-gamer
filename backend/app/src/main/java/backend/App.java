package backend;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {
    
    //public static final String CORS_ORIGIN = "http://71.82.254.4:3000";
    public static final String CORS_ORIGIN = "http://localhost:3000";

    BlogDatabase database;
    UsernameDatabase userDatabase;

    BlogRestController blogRestController;
    LoginRestController loginRestController;

    public App() {
        blogRestController = new BlogRestController();
        loginRestController = new LoginRestController(); 
    }

    @PreDestroy
    public void close() {
        BlogDatabase.save(database);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**");//.allowedOrigins("*");
                //.allowedOrigins("http://localhost:3000");
            }
        };
    }*/

    public static void closeSpringApp(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }
}

package backend;

import javax.annotation.PreDestroy;

import org.apache.tomcat.jni.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {
    
    BlogDatabase database;
    UsernameDatabase userDatabase;

    BlogRestController blogRestController;
    LoginRestController loginRestController;

    public App() {
        database = new BlogDatabase();
        userDatabase = new UsernameDatabase();
        blogRestController = new BlogRestController();
        loginRestController = new LoginRestController(userDatabase); 
    }

    @PreDestroy
    public void close() {
        BlogDatabase.save(database);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
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

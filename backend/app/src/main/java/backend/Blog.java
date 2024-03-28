package backend;

import org.springframework.stereotype.Component;

public class Blog {
    private long id;
    private String title;
    private String body;
    
    public Blog() {
        
    }

    public Blog(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {
        this.body = body;
    }

}

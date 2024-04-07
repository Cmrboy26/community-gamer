package backend;

import java.util.ArrayList;
import java.util.List;

public class Blog {

    public static final int MAX_TAG = 12;
    public static final int MAX_CATEGORY = 36;
    public static final int MAX_TITLE = 128;

    private long id;
    private String title, category;
    private String[] tags;
    private List<BlogSection> sections;
    
    public Blog() {
        sections = new ArrayList<BlogSection>();
        this.tags = new String[0];
    }

    public Blog(long id, String title, String category) {
        this();
        this.id = id;
        this.title = title;
        this.category = category;
    }

    public long getID() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    private BlogSection getSection(int index) {
        return sections.get(index);
    }
    public List<BlogSection> getSections() {
        return sections;
    }
    public String[] getTags() {
        return tags;
    }

    public void setID(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void addSection(String type, String data) {
        sections.add(new BlogSection(type, data));
    }

    public static BlogBuilder builder() {
        return new BlogBuilder();
    }

    public static class BlogSection {

        private String type, data;

        public BlogSection(String type, String data) {
            this.type = type;
            this.data = data;
        }

        public String getType() {
            return type;
        }
        public String getData() {
            return data;
        }

        public void setType(String type) {
            this.type = type;
        }
        public void setData(String data) {
            this.data = data;
        }
    } 

    public static class BlogBuilder {
        Blog blog = new Blog();

        public BlogBuilder ID(long id) {
            blog.setID(id);
            return this;
        }
        public BlogBuilder title(String title) {
            if (title.length() > MAX_TITLE) {
                throw new IllegalArgumentException("Title is too long.");
            }
            blog.setTitle(title);
            return this;
        }
        public BlogBuilder category(String category) {
            if (category.length() > MAX_CATEGORY) {
                throw new IllegalArgumentException("Category is too long.");
            }
            blog.setCategory(category);
            return this;
        }
        public BlogBuilder tags(String...data) {
            for (String tag : data) {
                if (tag.length() > MAX_TAG) {
                    throw new IllegalArgumentException("Tag \"" + tag.substring(0, MAX_TAG) + "\" is too long.");
                }
            }
            blog.tags = data;
            return this;
        }
        
        public enum SectionType {
            text,
            h1,
            h2
        }

        public BlogBuilder add(SectionType type, String data) {
            blog.addSection(type.name(), data);
            return this;
        }
        public Blog build() {
            return blog;
        }
    }

}

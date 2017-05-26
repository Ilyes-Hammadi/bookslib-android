package ilyeshammadi.booklib.models;

/**
 * Created by ilyes on 5/26/17.
 */

public class Book {
    private String name;
    private String description;
    private String slug;
    private String thumbnail_url;


    public Book(String name, String description, String slug, String thumbnail_url) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.thumbnail_url = thumbnail_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}

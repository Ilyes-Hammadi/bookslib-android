package ilyeshammadi.booklib.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static ilyeshammadi.booklib.utils.Constants.TAG;

/**
 * Created by ilyes on 5/26/17.
 */

public class Book {
    private int id;
    private String name;
    private String description;
    private String slug;
    private String thumbnail_url;
    private String linkToPdf;

    private boolean isLiked;
    private boolean isBookmarked;

    private int commentsCount, likesCount;

    public Book() {}

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Book(String name, String description, String slug, String thumbnail_url) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.thumbnail_url = thumbnail_url;
    }

    public Book(String name, String description, String slug, String thumbnail_url, int commentsCount, int likesCount) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.thumbnail_url = thumbnail_url;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
    }

    public Book(int id, String name, String description, String slug, String thumbnail_url, int commentsCount, int likesCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.thumbnail_url = thumbnail_url;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
    }

    public Book(int id, String name, String description, String slug, String thumbnail_url, String linkToPdf, int commentsCount, int likesCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.thumbnail_url = thumbnail_url;
        this.linkToPdf = linkToPdf;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
    }

    public Book(int id, String name, String description, String slug, String thumbnail_url, String linkToPdf,  int commentsCount, int likesCount, boolean isLiked, boolean isBookmarked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.thumbnail_url = thumbnail_url;
        this.linkToPdf = linkToPdf;
        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
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

    public String getThumbnail_urlL() {
        String url = getThumbnail_url();
        String chunks[] = url.split("\\.");

        chunks[4] = "LZZZZZZZ";

        url = "";

        if (chunks.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (String n : chunks) {
                nameBuilder.append(n.replace("'", "\\'")).append(".");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            url =  nameBuilder.toString();
        } else {
            url =  "";
        }


        Log.i(TAG, "getThumbnail_urlL: " + url);

        return url;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinkToPdf() {
        return linkToPdf;
    }

    public void setLinkToPdf(String linkToPdf) {
        this.linkToPdf = linkToPdf;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public static Book fromJson(JSONObject bookNode) {
        try {

//            User user  = User.fromJSON((JSONObject) articleNode.getJSONObject("user"));
//
//            JSONArray commentsNode = articleNode.getJSONArray("comments");
//
//
//            ArrayList<Comment> comments = new ArrayList<>();
//
//            for (int i = 0; i < commentsNode.length(); i++) {
//                comments.add(Comment.fromJSON(commentsNode.getJSONObject(i)));
//            }

            int id = bookNode.getInt("id");
            String slug = bookNode.getString("slug");
            String name = bookNode.getString("name");
            String description = bookNode.getString("description");
            String thumbnail = bookNode.getString("get_thulbnail");
            String linkToPdf = bookNode.getString("link_to_pdf");

            int commentsCount = bookNode.getInt("get_comments_count");
            int likesCount = bookNode.getInt("get_likes_count");

            boolean isLiked = bookNode.getBoolean("liked");
            boolean isBookmarked = bookNode.getBoolean("bookmarked");

            return new Book(id, name, description, slug, thumbnail, linkToPdf,commentsCount, likesCount, isLiked, isBookmarked);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }



}

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
    private String name;
    private String description;
    private String slug;
    private String thumbnail_url;

    private int commentsCount, likesCount;


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


            String name = bookNode.getString("name");
            String description = bookNode.getString("description");
            String thumbnail = bookNode.getString("get_thulbnail");

            int commentsCount = bookNode.getInt("get_comments_count");
            int likesCount = bookNode.getInt("get_likes_count");

            return new Book(name, description, "Slug", thumbnail, commentsCount, likesCount);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

}

package ilyeshammadi.booklib.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ilyes on 5/28/17.
 */

public class Comment {
    private User user;
    private String content;
    private String sentiment;

    public Comment(User user, String content, String sentiment) {
        this.user = user;
        this.content = content;
        this.sentiment = sentiment;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public static Comment fromJSON(JSONObject commentNode) {

       try {

            User user = User.fromJson(commentNode.getJSONObject("user"));
            String content = commentNode.getString("content");
            String sentiment = commentNode.getString("sentiment");

           Log.i("COMMENTDJ", "fromJSON: content: " + content);


            return new Comment(user,content, sentiment);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}

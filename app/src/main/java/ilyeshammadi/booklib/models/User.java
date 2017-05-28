package ilyeshammadi.booklib.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ilyes on 5/27/17.
 */

public class User {
    private String username;
    private String email;
    private String imageUrl;


    public User() {
        this.username = "";
        this.email = "";
        this.imageUrl = "";
    }

    public User(String username, String email, String imageUrl) {
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static User fromJson(JSONObject userNode) {

        try {
            String imageUrl = userNode.getString("image");
            String userName = userNode.getString("username");
            String email = userNode.getString("email");


            return new User(userName, email, imageUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

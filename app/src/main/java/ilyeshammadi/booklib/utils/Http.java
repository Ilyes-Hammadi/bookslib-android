package ilyeshammadi.booklib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;
import static ilyeshammadi.booklib.utils.Constants.TAG;

/**
 * Created by ilyes on 5/26/17.
 */

public class Http {

    public static Context context;

    public static String get(Context context, String u) {

        Http.context = context;

        
        String token = requestToken("ilyes", "cosplay222");

        StringBuilder sb = new StringBuilder();
        String jwtToken = sb.append("JWT").append(" ").append(token).toString();


        URL url = null;
        try {


            url = new URL(u);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Authorization", jwtToken);

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            urlConnection.disconnect();

            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR";

    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();


    static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    static boolean verfiyToken() {

        // Get token from shared pref
        String token = getPref(Http.context, "token");

        if (Objects.equals(token, "")) {
            return false;
        }


        try {
            JSONObject root = new JSONObject();
            root.put("token", token);

            String res = post(SERVER_URL + "/api-token-verify/", root.toString());

            JSONObject resNode = new JSONObject(res);

            if (resNode.has("token")) {
                return true;
            } else {
                return false;
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    static String requestToken(String username, String password) {


        try {

            JSONObject root = new JSONObject();
            root.put("username", username);
            root.put("password", password);

            String res = post(SERVER_URL + "/api-token-auth/", root.toString());

            JSONObject resNode = new JSONObject(res);

            if (resNode.has("token")) {

                String token = resNode.getString("token");

                // Save token in shared pref
                setPref(Http.context, "token", token);

                return token;
            } else {
                return "ERROR";
            }


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    public static void setPref(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();

    }


    public static String getPref(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    private static String getToken() {
        return get(Http.context, "token");
    }

}

package ilyeshammadi.booklib.asyntasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.utils.Http;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;
import static ilyeshammadi.booklib.utils.Constants.TAG;

/**
 * Created by ilyes on 5/27/17.
 */

public class BookmarkBookTask extends AsyncTask<Integer, Void, Boolean>{
    private final Context context;
    private ImageButton bookmarkBtn;


    public BookmarkBookTask(Context context, ImageButton bookmarkBtn) {
        this.context = context;
        this.bookmarkBtn = bookmarkBtn;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {


        try {
            Log.i(TAG, "doInBackground: Start click");

            // Set the book ID
            int bookId = params[0];

            // Construct URL
            String url = SERVER_URL + "/api/book/bookmark/" + String.valueOf(bookId);

            String res = Http.get(context, url);

            if (Objects.equals(res, "ERROR")) {
                return false;
            }

            JSONObject resNode = new JSONObject(res);
            String message = resNode.getString("message");

            Log.i(TAG, "doInBackground: " + message);

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {

            bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black_24dp);

        } else {
            bookmarkBtn.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }
    }
}

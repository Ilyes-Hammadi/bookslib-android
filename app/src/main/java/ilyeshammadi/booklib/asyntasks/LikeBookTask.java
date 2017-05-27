package ilyeshammadi.booklib.asyntasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.models.Book;
import ilyeshammadi.booklib.utils.Constants;
import ilyeshammadi.booklib.utils.Http;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;
import static ilyeshammadi.booklib.utils.Constants.TAG;

/**
 * Created by ilyes on 5/27/17.
 */

public class LikeBookTask extends AsyncTask<Integer, Void, Boolean> {

    private final Context context;
    private ImageButton likeBtn;
    private TextView likesCounter;
    private Book book;

    public LikeBookTask(Context context, ImageButton likeBtn, TextView likesCounter, Book book) {
        this.context = context;
        this.likeBtn = likeBtn;
        this.likesCounter = likesCounter;
        this.book = book;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {


        try {
            Log.i(TAG, "doInBackground: Start click");

            // Set the book ID
            int bookId = params[0];

            // Construct URL
            String url = SERVER_URL + "/api/book/like/" + String.valueOf(bookId);

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

            likeBtn.setImageResource(R.drawable.ic_favorite_black_24dp);

            book.setLikesCount(book.getLikesCount() + 1);
            book.setLiked(true);

            likesCounter.setText(book.getLikesCount() + "");
        } else {
            likeBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }
}

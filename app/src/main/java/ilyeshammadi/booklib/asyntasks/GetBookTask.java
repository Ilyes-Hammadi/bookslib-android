package ilyeshammadi.booklib.asyntasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ilyeshammadi.booklib.activities.BookDetailActivity;
import ilyeshammadi.booklib.adapters.CommentAdapter;
import ilyeshammadi.booklib.models.Book;
import ilyeshammadi.booklib.models.Comment;
import ilyeshammadi.booklib.models.User;
import ilyeshammadi.booklib.utils.Http;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;
import static ilyeshammadi.booklib.utils.Constants.TAG;

/**
 * Created by ilyes on 5/28/17.
 */

public class GetBookTask extends AsyncTask<Integer, Void, ArrayList<Comment>> {

    private Context context;
    private CommentAdapter adapter;
    private Book mBook;

    public GetBookTask(Context context, CommentAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<Comment> doInBackground(Integer... params) {

        int bookId = params[0];
        String data = Http.get(context,SERVER_URL + "/api/books/" + bookId);

        try {
            JSONObject bookNode = new JSONObject(data);

            Log.i(TAG, "doInBackground: Get Article " + bookNode);

            Book book = Book.fromJson(bookNode);

            Log.i(TAG, "doInBackground: " + book.getComments().size());


            return book.getComments();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Comment> comments) {
        adapter.swap(comments);
    }
}


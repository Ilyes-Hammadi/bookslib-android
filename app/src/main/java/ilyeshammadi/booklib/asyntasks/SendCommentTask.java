package ilyeshammadi.booklib.asyntasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ilyeshammadi.booklib.utils.Http;

import static ilyeshammadi.booklib.utils.Constants.TAG;

/**
 * Created by ilyes on 5/28/17.
 */

public class SendCommentTask extends AsyncTask<String, Void, Boolean> {

    private Context context;

    public SendCommentTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Integer bookId = Integer.valueOf(params[0]);
        String commentText = params[1];

        Log.i(TAG, "doInBackground: BookId" + bookId);

        Http.addComment(context, bookId, commentText);

        return null;
    }
}

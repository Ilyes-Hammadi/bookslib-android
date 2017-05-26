package ilyeshammadi.booklib.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.models.Book;
import ilyeshammadi.booklib.utils.Http;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;

public class BookDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;

    private TextView mBookTitle;
    private TextView mBookDescription;
    private ImageView mBookThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBar = getSupportActionBar();

        // Get all the views
        mBookDescription = (TextView) findViewById(R.id.book_description_tv);
        mBookThumbnail = (ImageView) findViewById(R.id.book_thumbnail_iv);

        // Get the intent
        Intent intent = getIntent();

        // Extract the book id
        int id = intent.getIntExtra("book-id", 1);
        String bookName = intent.getStringExtra("book-name");

        mActionBar.setTitle(bookName);

        new GetBookTask().execute(id);


    }


    private class GetBookTask extends AsyncTask<Integer, Void, Book> {


        @Override
        protected Book doInBackground(Integer... params) {

            int bookId = params[0];
            String data = Http.get(SERVER_URL + "/api/books/" + bookId);

            try {
                JSONObject bookNode = new JSONObject(data);

                return Book.fromJson(bookNode);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Book book) {
            // Set data into the views
            book.getThumbnail_urlL();
            Picasso.with(BookDetailActivity.this).load(book.getThumbnail_urlL()).into(mBookThumbnail);
            mBookDescription.setText(book.getDescription());

        }
    }



}

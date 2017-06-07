package ilyeshammadi.booklib.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.adapters.ListBookAdapter;
import ilyeshammadi.booklib.adapters.SimilarBooksAdapter;
import ilyeshammadi.booklib.models.Book;
import ilyeshammadi.booklib.utils.Http;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;

public class BookDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;

    private TextView mBookAuthor;
    private TextView mBookDescription;
    private ImageView mBookThumbnail;
    private String pdfUrl;
    private Book mBook;

    private RecyclerView mSimilarBooksRV;
    private SimilarBooksAdapter mSimilarAdapter;

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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBar = getSupportActionBar();

        // Get all the views
        mBookAuthor = (TextView) findViewById(R.id.book_author_tv);
        mBookDescription = (TextView) findViewById(R.id.book_description_tv);
        mBookThumbnail = (ImageView) findViewById(R.id.book_thumbnail_iv);

        // Get the intent
        Intent intent = getIntent();

        // Extract the book id
        final int id = intent.getIntExtra("book-id", 1);
        String bookName = intent.getStringExtra("book-name");

        mActionBar.setTitle(bookName);

        new GetBookTask().execute(id);


        // Setup bottom bar
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_share) {
                    if (mBook != null) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, mBook.getName());
                        i.putExtra(Intent.EXTRA_TEXT, SERVER_URL + "/books/detail/" + mBook.getSlug());
                        startActivity(Intent.createChooser(i, "Share Book"));
                    }
                } else if (tabId == R.id.tab_comment) {
                    Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                    intent.putExtra("book-id", id);
                    startActivity(intent);
                }

            }
        });

    }

    private void setupSimilarBooksRecycleView() {
        mSimilarBooksRV = (RecyclerView) findViewById(R.id.similar_books_rv);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mSimilarBooksRV.setLayoutManager(mLayoutManager);
        mSimilarBooksRV.setItemAnimator(new DefaultItemAnimator());

        mSimilarAdapter = new SimilarBooksAdapter(this, new ArrayList<Book>());
        mSimilarBooksRV.setAdapter(mSimilarAdapter);
    }

    private class GetBookTask extends AsyncTask<Integer, Void, Book> {


        @Override
        protected Book doInBackground(Integer... params) {

            int bookId = params[0];
            String data = Http.get(getApplicationContext(), SERVER_URL + "/api/books/" + bookId);

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
            mBook = book;

            // Set data into the views
            book.getThumbnail_urlL();
            Picasso.with(BookDetailActivity.this).load(book.getThumbnail_urlL()).into(mBookThumbnail);

            // Set the author name
            mBookAuthor.setText("By " + book.getAuthor());

            // Set description
            mBookDescription.setText(book.getDescription());

            pdfUrl = book.getLinkToPdf();

            // Get similar books
            new GetSimilarBooksTask().execute(mBook.getId());

        }
    }

    private class GetSimilarBooksTask extends AsyncTask<Integer, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(Integer... params) {

            int bookId = params[0];

            String data = Http.get(getApplicationContext(), SERVER_URL + "/api/book/similar/?book_id=" + String.valueOf(bookId));
            ArrayList<Book> books = new ArrayList<>();

            try {

                JSONObject rootNode = new JSONObject(data);

                JSONArray results = rootNode.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {

                    JSONObject bookNode = (JSONObject) results.get(i);

                    books.add(Book.fromJson(bookNode));
                }

                return books;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {

            for (int i = 0; i < books.size(); i++) {
                Log.i("GOGO", "onPostExecute: " + books.get(i).getName());
            }

            // Setup similar books recycle view
            setupSimilarBooksRecycleView();

            mSimilarAdapter.swap(books);
        }
    }

}

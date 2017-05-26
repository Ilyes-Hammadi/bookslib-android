package ilyeshammadi.booklib.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.adapters.ListBookAdapter;
import ilyeshammadi.booklib.models.Book;

public class MainActivity extends AppCompatActivity {

    List<Book> mBooks = new ArrayList<>();
    RecyclerView mListBooksRL;
    ListBookAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the views
        mListBooksRL = (RecyclerView) findViewById(R.id.list_books);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListBooksRL.setLayoutManager(mLayoutManager);
        mListBooksRL.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListBookAdapter(mBooks);
        mListBooksRL.setAdapter(mAdapter);

        prepareData();

    }


    private void prepareData() {
        for (int i = 0; i < 10; i++) {
            Book book = new Book("Lolo", "Lolo", "Lolo", "htt://lolo.com");
            mBooks.add(book);
        }
    }

}

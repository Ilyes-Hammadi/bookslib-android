package ilyeshammadi.booklib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.adapters.CommentAdapter;
import ilyeshammadi.booklib.asyntasks.GetBookTask;
import ilyeshammadi.booklib.models.Comment;
import ilyeshammadi.booklib.models.User;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Get the views
        final EditText addCommentET = (EditText) findViewById(R.id.add_comment_et);
        ImageButton sendCommentBtn = (ImageButton) findViewById(R.id.send_comment_btn);


        // Get the article id from the intent
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("book-id", 0);


        // Get views
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_comments_rv);
        final CommentAdapter adapter = new CommentAdapter(this, new ArrayList<Comment>());

        // Get the article data from the server
        new GetBookTask(this, adapter).execute(id);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

}

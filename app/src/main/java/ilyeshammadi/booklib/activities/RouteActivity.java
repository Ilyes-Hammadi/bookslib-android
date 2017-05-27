package ilyeshammadi.booklib.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.utils.Http;

public class RouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);


        // If user is signed in go to list books
        // Otherwise go to signing
        if (Http.isUserLoggedIn(this)) {
            startActivity(new Intent(this, ListBookActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }
}

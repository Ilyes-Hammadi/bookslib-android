package ilyeshammadi.booklib.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.utils.Http;

public class RouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);


        // This solution will leak memory!  Don't use!!!
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // If user is signed in go to list books
                // Otherwise go to signing
                if (Http.isUserLoggedIn(RouteActivity.this)) {
                    startActivity(new Intent(RouteActivity.this, ListBookActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(RouteActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 2000);


    }
}

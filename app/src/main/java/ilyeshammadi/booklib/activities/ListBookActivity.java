package ilyeshammadi.booklib.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.adapters.ListBookAdapter;
import ilyeshammadi.booklib.asyntasks.GetListBooksTask;
import ilyeshammadi.booklib.models.Book;
import ilyeshammadi.booklib.models.User;
import ilyeshammadi.booklib.utils.Http;
import ilyeshammadi.booklib.utils.Utils;

import static ilyeshammadi.booklib.utils.Constants.SERVER_URL;
import static ilyeshammadi.booklib.utils.Constants.TAG;

public class ListBookActivity extends AppCompatActivity {

    List<Book> mBooks = new ArrayList<>();
    RecyclerView mListBooksRL;
    ListBookAdapter mAdapter;
    MaterialSearchView mSearchView;

    String[] mSearchResults = new String[]{};
    ArrayList<Book> mSearchbooks = new ArrayList<>();

    ArrayAdapter<String> mSearchAdapter;

    private IProfile mProfile;
    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        //getSupportActionBar().setLogo(R.drawable.ic_menu_black_24dp);

        // Get the views
        mListBooksRL = (RecyclerView) findViewById(R.id.list_books);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListBooksRL.setLayoutManager(mLayoutManager);
        mListBooksRL.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListBookAdapter(this, mBooks);
        mListBooksRL.setAdapter(mAdapter);

        // Get data from server
        new GetListBooksTask(getApplicationContext(), mAdapter).execute();

        setupLoggedinUserDrawer();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer();
            }
        });

        // Search view
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchAdapter = new ArrayAdapter<>(this, R.layout.search_list_item, mSearchResults);


        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                if (mSearchResults != null) {
                    for (int i = 0; i < mSearchResults.length; i++) {
                        if (Objects.equals(query, mSearchResults[i])) {
                            Intent intent = new Intent(ListBookActivity.this, BookDetailActivity.class);
                            intent.putExtra("book-id", mSearchbooks.get(i).getId());
                            intent.putExtra("book-name", mSearchbooks.get(i).getName());
                            startActivity(intent);
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                new SearchTask().execute(newText);
                return true;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                mSearchView.setAdapter(mSearchAdapter);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchView.setQuery((String) mSearchAdapter.getItem(position), false);
            }
        });

    }

    private void setupLoggedinUserDrawer() {
        // Set Navigation Drawer
        mDrawer = new DrawerBuilder()
                .withActivity(ListBookActivity.this)
                .build();


        // Get username from shared pref
        String username = Http.getPref(this, "username");

        Log.i("USERNAME", "setupLoggedinUserDrawer: " + username);

        new GetUserDataTask(this).execute(username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            Http.logout();
            startActivity(new Intent(ListBookActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    public class SearchTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            mSearchbooks = new ArrayList<>();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String searchTerm = params[0];

            if (searchTerm.isEmpty()) {
                Log.i(TAG, "doInBackground: Empty");
                return new ArrayList<>();
            }

            String data = Http.get(getApplicationContext(), SERVER_URL + "/api/search/?search=" + searchTerm);
            ArrayList<String> booksNameList = new ArrayList<>();

            try {

                JSONObject rootNode = new JSONObject(data);

                JSONArray results = rootNode.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject book = results.getJSONObject(i);

                    // Copy the name in the sugeestion array
                    booksNameList.add(book.getString("name"));

                    int id = book.getInt("id");
                    String name = book.getString("name");

                    mSearchbooks.add(new Book(id, name));

                }


                return booksNameList;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<String> stringArr) {

            String[] booksNameArr = new String[stringArr.size()];
            booksNameArr = stringArr.toArray(booksNameArr);

            mSearchResults = booksNameArr;

            mSearchAdapter.clear();
            mSearchAdapter.addAll(mSearchResults);
            mSearchAdapter.notifyDataSetChanged();

            for (int i = 0; i < mSearchbooks.size(); i++) {
                Log.i(TAG, "onPostExecute: " + mSearchbooks.get(i).getName());
            }

        }
    }


    public class GetUserDataTask extends AsyncTask<String, Void, User> {

        private Context context;
        Bitmap image;

        public GetUserDataTask(Context context) {
            this.context = context;
        }

        @Override
        protected User doInBackground(String... params) {


            try {
                // Get the username
                String username = params[0];

                Log.i("USERNAME", "doInBackground: " + username);

                // Construc String
                String url = SERVER_URL + "/api/users/" + username + '/';

                Log.i("USERNAME", "doInBackground: " + url);

                // Get data from server
                String data = Http.get(context, url);

                Log.i("USERNAME", "doInBackground: " + data);

                // If there are no errors
                if (Objects.equals(data, "ERROR")) {
                    return new User("Error", "Error", "");
                }

                // Parse Json
                JSONObject root = new JSONObject(data);

                String userName = root.getString("username");
                String email = root.getString("email");
                String imageUrl = root.getString("image");


                image = Utils.drawableFromUrl(imageUrl);

                return new User(userName, email, imageUrl);


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }


            return new User();
        }

        @Override
        protected void onPostExecute(User user) {


            // Create the AccountHeader
            AccountHeader headerResult = new AccountHeaderBuilder()
                    .withHeaderBackground(R.drawable.black_background)
                    .withActivity(ListBookActivity.this)
                    .addProfiles(
                            new ProfileDrawerItem().withName(user.getUsername()).withEmail(user.getEmail()).withIcon(image)
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .build();

            // Set Navigation Drawer
            mDrawer = new DrawerBuilder()
                    .withAccountHeader(headerResult)
                    .withActivity(ListBookActivity.this)
                    .build();


        }
    }

}

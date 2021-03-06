package ilyeshammadi.booklib.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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

    Boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RelativeLayout mNoInternetIcon;
    private RelativeLayout mProgeressBar;

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
        mNoInternetIcon = (RelativeLayout) findViewById(R.id.no_internet_icon);
        mProgeressBar = (RelativeLayout) findViewById(R.id.progressBar);


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


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GOGO", "onStart: ");
        if(Http.isConnectingToInternet(this)) {
            // If there are no data
            if (mBooks.size() == 0) {
                initRecycleViewLis();
            }
        } else {
            showSnackbar();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("GOGO", "onResume: ");
        if(!Http.isConnectingToInternet(this)) {
            showSnackbar();
        }
    }

    private void hideListAndShowIcon() {
        mListBooksRL.setVisibility(View.GONE);
        mNoInternetIcon.setVisibility(View.VISIBLE);
    }

    private void showListAndHideIcon() {
        mListBooksRL.setVisibility(View.VISIBLE);
        mNoInternetIcon.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgeressBar.setVisibility(View.VISIBLE);
        mListBooksRL.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        mProgeressBar.setVisibility(View.GONE);
        mListBooksRL.setVisibility(View.VISIBLE);
    }


    private void showSnackbar() {
        hideListAndShowIcon();
        Snackbar.make(mListBooksRL, "No internet connection", Snackbar.LENGTH_INDEFINITE).setAction("Go online", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        }).show();
    }

    private void initRecycleViewLis() {

        // Show the list
        showListAndHideIcon();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListBooksRL.setLayoutManager(mLayoutManager);
        mListBooksRL.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListBookAdapter(this, mBooks);
        mListBooksRL.setAdapter(mAdapter);




        mListBooksRL.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {



                if(dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();


                    if (!loading) {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            Toast.makeText(ListBookActivity.this, "New Data is Loading", Toast.LENGTH_SHORT).show();

                            // Get the next page link
                            String nextLink = Http.getPref(getApplicationContext(), "next");

                            if (nextLink != null) {
                                // Get data from server
                                new GetListBooksTask(getApplicationContext(), mAdapter, loading).execute(nextLink);
                            }

                        }
                    }
                }
            }
        });

        // Get first chunk of data from the server
        new GetListBooksTask(getApplicationContext(), mAdapter).execute();


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
            Http.logout(getApplicationContext());
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


    private class SearchTask extends AsyncTask<String, Void, ArrayList<String>> {

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


    private class GetUserDataTask extends AsyncTask<String, Void, User> {

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

                User user = User.fromJson(root);

                image = Utils.drawableFromUrl(user.getImageUrl());

                return user;


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
                    .addDrawerItems(
                            new DividerDrawerItem(),
                            new SecondaryDrawerItem().withIdentifier(1).withName("Logout").withIcon(R.drawable.ic_logout)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem.getIdentifier() == 1) {
                                Http.logout(getApplicationContext());
                                return true;
                            }
                            return false;
                        }
                    })
                    .build();


        }
    }

    private class GetListBooksTask extends AsyncTask<String, Void, ArrayList<Book>> {

        private ListBookAdapter adapter;
        private Context context;
        private ArrayList<Book> booksList = new ArrayList<>();
        private Boolean loading = true;

        public GetListBooksTask(Context context, ListBookAdapter adapter) {
            this.adapter = adapter;
            this.context = context;
        }

        public GetListBooksTask( Context context, ListBookAdapter adapter, boolean loading) {
            this.adapter = adapter;
            this.context = context;
            this.loading = loading;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            loading = true;

            String url;
            if (params.length > 0){
                url = params[0];
            }else {
                url = SERVER_URL + "/api/books/?format=json";
            }

            String data = Http.get(this.context, url);

            Log.i(TAG, "doInBackground: " + data);


            try {
                JSONObject topLevel = null;

                topLevel = new JSONObject(data);

                JSONArray results = topLevel.getJSONArray("results");

                // Save the next page link
                String nextLink = topLevel.getString("next");
                if (nextLink != null) {
                    Http.setPref(context, "next", nextLink);
                }

                Book book = null;

                for (int i = 0; i < results.length(); i++) {
                    JSONObject bookNode = (JSONObject) results.get(i);
                    book = Book.fromJson(bookNode);
                    booksList.add(book);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return booksList;
        }


        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            hideProgressBar();
            this.loading = false;
            this.adapter.swap(books);
        }
    }
}

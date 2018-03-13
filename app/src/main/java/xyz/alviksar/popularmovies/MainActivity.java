package xyz.alviksar.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;
import xyz.alviksar.popularmovies.model.PopularMovie;
import xyz.alviksar.popularmovies.utils.PopularMoviesPreferences;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Displays movies in the main layout via a grid of their corresponding poster thumbnails
 */

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PosterAdapter.PosterAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private Spinner mSpinner;

    private PosterAdapter mPosterAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    // The poster width on screen in inches
    public static final float POSTER_WIDTH_INCHES = 1.0f;

    private static final int MOVIES_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // A debug tool: http://facebook.github.io/stetho/
        Stetho.initializeWithDefaults(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mRecyclerView = findViewById(R.id.rv_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);

        // Calculate the number of columns in the grid
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int mColumnWidthPixels;
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            mColumnWidthPixels = Math.round(POSTER_WIDTH_INCHES * metrics.ydpi);

        } else {   // ORIENTATION_PORTRAIT
            mColumnWidthPixels = Math.round(POSTER_WIDTH_INCHES * metrics.xdpi);

        }
        int columns = Math.max(1, metrics.widthPixels / mColumnWidthPixels);


        GridLayoutManager layoutManager =
                new GridLayoutManager(this, columns);

        mRecyclerView.setLayoutManager(layoutManager);
        //  mRecyclerView.setHasFixedSize(true);

        // Create PosterAdapter with this context and this OnClickHandler
        mPosterAdapter = new PosterAdapter(this, this);
        // Attach the adapter to the RecyclerView
        mRecyclerView.setAdapter(mPosterAdapter);

        // Get the sort criteria and initialize a loader
        String sort = PopularMoviesPreferences.getSort(this);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(getResources().getString(R.string.pref_sort_key), sort);

        // Check network connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if ((networkInfo != null && networkInfo.isConnected())
                || (sort.equals(getString(R.string.show_favorites)))) {
            showLoading();

            getLoaderManager().initLoader(MOVIES_LOADER_ID, queryBundle, this);
        } else {
            // Set no connection error message
            showErrorMessage(R.string.no_connection_error_msg);
        }
    }

    /**
     * This method will hide everything except the TextView error message and set the appropriate text to it.
     */
    private void showErrorMessage(int msgResId) {
        mPosterAdapter.swapData(null);
        mLoadingIndicator.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(msgResId);
    }

    /**
     * This method will make the loading indicator visible and hide the RecyclerView and error
     * message.
     */
    private void showLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * This method will make the RecyclerView visible and hide the error message and
     * loading indicator.
     */
    private void showData() {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sort = bundle.getString(getResources().getString(R.string.pref_sort_key));
        Uri uri = null;
        String endpoint = "";
        if (getString(R.string.sort_by_most_popular).equals(sort)) {
            uri = PopularMoviesContract.BASE_CONTENT_URI.buildUpon()
                    .appendPath(PopularMoviesContract.PATH_THEMOVIEDB)
                    .appendPath(PopularMoviesContract.POPULAR_ENDPOINT)
                    .build();
        } else if (getString(R.string.sort_by_top_rated).equals(sort)) {
            uri = PopularMoviesContract.BASE_CONTENT_URI.buildUpon()
                    .appendPath(PopularMoviesContract.PATH_THEMOVIEDB)
                    .appendPath(PopularMoviesContract.TOP_RATED_ENDPOINT)
                    .build();
        } else if (getString(R.string.show_favorites).equals(sort)) {
            uri = PopularMoviesContract.MoviesEntry.CONTENT_URI;
        }
        if (uri != null) {
            return new CursorLoader(this,
                    uri,
                    null,
                    null,
                    null,
                    null);
        } else {
            return null;
        }
        //  return new PopularMoviesLoader(this, args, POSTER_WIDTH_INCHES);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            mPosterAdapter.swapData(cursor);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (cursor.getCount() != 0) {
                showData();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPosterAdapter.swapData(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        /*
        Set spinner into menu bar
        Thanks to Dércia Silva
        http://www.viralandroid.com/2016/03/how-to-add-spinner-dropdown-list-to-android-actionbar-toolbar.html
        */
        MenuItem item = menu.findItem(R.id.spinner);
        mSpinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        // Set spinner to the right state
        int defPosition = adapter.getPosition(PopularMoviesPreferences.getSort(this));
        mSpinner.setSelection(defPosition);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSpinner.getSelectedItem() != null) {
                    String sort = (String) mSpinner.getSelectedItem();
                    PopularMoviesPreferences.setSort(getApplicationContext(), sort);
                    updateMovies(sort);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }

    /**
     * This method tells an AsyncTaskLoader to perform the HTTP-request with new sort criteria.
     */
    private void updateMovies(String sort) {

        if (TextUtils.isEmpty(sort)) {
            showErrorMessage(R.string.no_sort_error_msg);
            return;
        }

        // Put sort criteria to the bundle for Loader
        Bundle queryBundle = new Bundle();
        queryBundle.putString(getResources().getString(R.string.pref_sort_key), sort);

        /*
         * We give the LoaderManager an ID and it returns a loader (if one exists). If
         * one doesn't exist, we tell the LoaderManager to create one. If one does exist, we tell
         * the LoaderManager to restart it.
         */
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> moviesLoader = loaderManager.getLoader(MOVIES_LOADER_ID);
        if (moviesLoader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, queryBundle, this);
        }
    }

    /**
     * This method is for responding to clicks from our grid.
     *
     * @param movie The movie that was clicked
     */
    @Override
    public void onClick(PopularMovie movie) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra(getString(R.string.movie_parcel_key), movie);
        startActivity(movieDetailIntent);
    }

}


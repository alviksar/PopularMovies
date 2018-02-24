package xyz.alviksar.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import xyz.alviksar.popularmovies.model.PopularMovie;

/**
Displays in the main layout via a grid of their corresponding movie poster thumbnails.
 */

public class MainActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<List<PopularMovie>>  {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    private PosterAdapter mPosterAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final float POSTER_WIDTH_INCHES = 1.0f;
    private static final int MOVIES_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);

        AutoSpanGridLayoutManager layoutManager =
                new AutoSpanGridLayoutManager(this, POSTER_WIDTH_INCHES);

        mRecyclerView.setLayoutManager(layoutManager);
        //  mRecyclerView.setHasFixedSize(true);

        // Attach the adapter to the RecyclerView
        mPosterAdapter = new PosterAdapter(this);
        mRecyclerView.setAdapter(mPosterAdapter);

        // Check network connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            showLoading();
            // Initialize the loader
            getLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
        } else {
            // Set no connection error message
            showErrorMessage(R.string.no_connection);
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
        // Otherwise, display error
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
    public Loader<List<PopularMovie>> onCreateLoader(int i, Bundle bundle) {
        return new PopularMoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<PopularMovie>> loader, List<PopularMovie> popularMovies) {
        if (popularMovies != null) {
            mPosterAdapter.swapData(popularMovies);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (popularMovies.size() != 0) {
                showData();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PopularMovie>> loader) {
        mPosterAdapter.swapData(null);
    }
}

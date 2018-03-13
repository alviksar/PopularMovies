package xyz.alviksar.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;
import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;
import xyz.alviksar.popularmovies.model.PopularMovie;

/**
 * Displays a screen with additional information
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PopularMovie mMovie;

    private static final int MOVIE_DETAIL_LOADER_ID = 11;
    // The button to mark a movie as favorite
    private Button mMarkButton;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getString(R.string.movie_parcel_key), mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.detail_activity);


        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_parcel_key))) {
            mMovie = getIntent().getParcelableExtra(getString(R.string.movie_parcel_key));
        } else {
            mMovie = savedInstanceState.getParcelable(getString(R.string.movie_parcel_key));
        }

        setTitle(mMovie.getTitle());

        // Uses dataBinding
        DetailActivityBinding binding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.detail_activity);
        DetailPagerAdapter adapter = new DetailPagerAdapter(this, String.valueOf(mMovie.getId()), getSupportFragmentManager());
        binding.setPagerAdapter(adapter);
        binding.slidingTabs.setupWithViewPager(binding.viewpager);
        binding.setPopularMovie(mMovie);

        // Setup button to mark movie as favorite
        mMarkButton = findViewById(R.id.btn_mark);
        mMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = mMovie.getIsFavorite();
                try {
                    if (!isFavorite) {
                        isFavorite = markMovieAsFavorite();
                    } else {
                        isFavorite = !removeMovieFromFavorites();
                    }
                } finally {
                    mMovie.setIsFavorite(isFavorite);
                    showMovieState();
                }
            }
        });

        // Connect our activity into the loader lifecycle
        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);
    }

    private boolean markMovieAsFavorite() {

        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.MoviesEntry._ID, mMovie.getId());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_TITLE, mMovie.getTitle());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_ORIGINALTITLE, mMovie.getOriginalTitle());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_POSTER, mMovie.getPoster());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_PLOTSYNOPSIS, mMovie.getPlotSynopsis());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_USERRATING, mMovie.getUserRating());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_POPULARITY, mMovie.getPopularity());
        values.put(PopularMoviesContract.MoviesEntry.COLUMN_RELEASEDATE, mMovie.getReleaseDate());

        Uri newUri = getContentResolver().insert(
                PopularMoviesContract.MoviesEntry.CONTENT_URI,   // the user dictionary content URI
                values                          // the values to insert
        );
        return (newUri != null);
    }

    private boolean removeMovieFromFavorites() {
        int rowDeleted = getContentResolver().delete(
                ContentUris.appendId(
                        PopularMoviesContract.MoviesEntry.CONTENT_URI.buildUpon(),
                        mMovie.getId()
                ).build(),
                null,
                null
        );
        return (rowDeleted != 0);

    }

    private void showMovieState() {
        if (mMovie.getIsFavorite()) {
            mMarkButton.setText(R.string.remove_from_favorite);
            mMarkButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGold));
        } else {
            mMarkButton.setText(R.string.mark_as_favorite);
            mMarkButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case MOVIE_DETAIL_LOADER_ID:
                Uri uri = ContentUris.appendId(
                        PopularMoviesContract.MoviesEntry.CONTENT_URI.buildUpon(),
                        mMovie.getId()
                ).build();
                String[] projection = {
                        PopularMoviesContract.MoviesEntry._ID
                };
                return new CursorLoader(this,
                        uri,
                        projection,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        int rows;
        if (cursor != null) {
            rows = cursor.getCount();
            // cursor.close();
            if (rows == 1) {
                mMovie.setIsFavorite(true);
            } else if (rows == 0) {
                mMovie.setIsFavorite(false);
            } else {
                throw new RuntimeException("Inconsistent data.");
            }
        }
        showMovieState();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}

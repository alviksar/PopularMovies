package xyz.alviksar.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;
import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;
import xyz.alviksar.popularmovies.model.PopularMovie;

/**
 * Displays a screen with additional information
 */
public class DetailActivity extends AppCompatActivity {

    private PopularMovie mMovie;

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
        mMovie.setIsFavorite(checkMovieIsFavorite());
        if (mMovie.getIsFavorite()) {
            Toast.makeText(this, "It is a favorite movie", Toast.LENGTH_LONG).show();
        }
        //   TODO: Move to the loader
        // showMovieState();

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
        // Show a toast message depending on whether or not the insertion was successful
//        if (newUri != null) {
//            // If the new content URI is null, then there was an error with insertion.
//            Toast.makeText(this, R.string.marked_message,
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        } else {
//            // Otherwise, the insertion was successful and we can display a toast.
//            Toast.makeText(this, R.string.mark_failed_message,
//                    Toast.LENGTH_SHORT).show();
//            return false;
//
//        }
    }

    private boolean removeMovieFromFavorites() {
        //   ContentValues values = new ContentValues();
        //  values.put(PopularMoviesContract.MoviesEntry._ID, mMovie.getId());
        int rowDeleted = getContentResolver().delete(
                ContentUris.appendId(
                        PopularMoviesContract.MoviesEntry.CONTENT_URI.buildUpon(), mMovie.getId()).build(),
                null,
                null
        );
        return (rowDeleted != 0);
//        // Show a toast message depending on whether or not the insertion was successful
//        if (rowDeleted != 0) {
//            // If the new content URI is null, then there was an error with insertion.
//            Toast.makeText(this, R.string.mark_removed_message,
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        } else {
//            // Otherwise, the insertion was successful and we can display a toast.
//            Toast.makeText(this, R.string.mark_failed_message,
//                    Toast.LENGTH_SHORT).show();
//            return false;
//
//        }
    }

    private boolean checkMovieIsFavorite() {

        Cursor cursor = getContentResolver().query(
                ContentUris.appendId(
                        PopularMoviesContract.MoviesEntry.CONTENT_URI.buildUpon(), mMovie.getId()).build(),
                null,
                null,
                null,
                null);

        int rows = 0;
        if (cursor != null) {
            rows = cursor.getCount();
            cursor.close();
        }
        if (rows > 1) throw new RuntimeException("Inconsistent data.");
        return (rows == 1);

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
}

package xyz.alviksar.popularmovies;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;
import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;
import xyz.alviksar.popularmovies.model.PopularMovie;
import xyz.alviksar.popularmovies.utils.TheMovieDbHttpUtils;

/**
 * Displays a screen with additional information
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    DetailActivityBinding mBinding;

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
        mBinding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.detail_activity);
        DetailPagerAdapter adapter = new DetailPagerAdapter(this, String.valueOf(mMovie.getId()), getSupportFragmentManager());
        mBinding.setPagerAdapter(adapter);
        mBinding.slidingTabs.setupWithViewPager(mBinding.viewpager);
        mBinding.setPopularMovie(mMovie);

        // Setup button to mark movie as favorite
        mMarkButton = findViewById(R.id.btn_mark);
        mMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = mMovie.getIsFavorite();
                try {
                    if (!isFavorite) {
                        markMovieAsFavorite();
                    } else {
                        removeConfirmationDialog();
                    }
                } finally {
                    showMovieState();
                }
            }
        });

        // Connect our activity into the loader lifecycle
        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);
    }

    private void markMovieAsFavorite() {

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
                PopularMoviesContract.MoviesEntry.CONTENT_URI,
                values                          // the values to insert
        );

        // Save this poster to local file
        savePosterImageToFile();
    }

    private void removeMovieFromFavorites() {
        int rowDeleted = getContentResolver().delete(
                ContentUris.appendId(
                        PopularMoviesContract.MoviesEntry.CONTENT_URI.buildUpon(),
                        mMovie.getId()
                ).build(),
                null,
                null
        );
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
        // Define if movie is favorite
        int rows;
        if (cursor != null) {
            rows = cursor.getCount();
            // cursor.close();
            if (rows == 1) {
                mMovie.setIsFavorite(true);
            } else if (rows == 0) {
                mMovie.setIsFavorite(false);
            } else {
                Log.wtf(TAG, "Inconsistent data.");
                throw new RuntimeException("Inconsistent data.");
            }
        }
        showMovieState();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void removeConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        boolean result = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.remove_movie_dialog_question);
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Remove" button, so remove the movie from favorites
                removeMovieFromFavorites();
                deletePosterFile();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void savePosterImageToFile() {
        Picasso.with(this).load(TheMovieDbHttpUtils.getFullPathToPoster(mMovie.getPoster()))
                .into(picassoImageTarget(getApplicationContext(),
                        PopularMoviesContract.IMAGE_DIR,
                        mMovie.getPoster()));

    }

    private void deletePosterFile() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(PopularMoviesContract.IMAGE_DIR, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, mMovie.getPoster());
        if (myImageFile.delete())
            Log.d(TAG,"Image on the disk deleted successfully!");
    }

    /*
    * http://www.codexpedia.com/android/android-download-and-save-image-through-picasso/
    */
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(TAG, "image saved to " + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }
}

package xyz.alviksar.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import xyz.alviksar.popularmovies.model.PopularMovie;
import xyz.alviksar.popularmovies.utils.TheMovieDbHttpUtils;
import xyz.alviksar.popularmovies.utils.TheMovieDbJsonUtils;


/**
 * AsyncTaskLoader for loading a list of movies
 */

public class PopularMoviesLoader extends AsyncTaskLoader<List<PopularMovie>> {

    private static final String TAG = PopularMoviesLoader.class.getSimpleName();

    // mJson will store the raw JSON result
    private List<PopularMovie> sStoredResult;
    // Current sort criteria
    private String sSort = "";

    private Boolean mSortChanged;
    /*
    You might have noticed the static keyword when declaring the JsonAsyncTaskLoader.
    It is incredibly important that your Loader does not contain any reference
    to any containing Activity or Fragment and that includes the implicit reference created
    by non-static inner classes.
    Obviously, if you’re not declaring your Loader as an inner class,
    you won’t need the static keyword.
    https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832
    */

    public PopularMoviesLoader(Context context, Bundle args, float posterWidthInches) {
        super(context);
        String sort = args.getString(context.getResources().getString(R.string.pref_sort_key));
        mSortChanged = !sSort.equals(sort);
        if (mSortChanged) {
            sSort = sort;
        }
        TheMovieDbHttpUtils.init(context, posterWidthInches);
    }

    @Override
    protected void onStartLoading() {
        // If we have already cached valid results, just deliver them
        if (sStoredResult != null && !mSortChanged) {
            deliverResult(sStoredResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<PopularMovie> loadInBackground() {

        List<PopularMovie> popularMovieList = null;
        try {
            String rawJson =  TheMovieDbHttpUtils.getMoviesBy(sSort);
            if (TextUtils.isEmpty(rawJson)) {
                Log.e(TAG, "No json received.");
            }
            popularMovieList = TheMovieDbJsonUtils.getMoviesFromJson(rawJson);
        } catch (Exception e) {
            Log.e(TAG, "No response received.", e);
        }
        return popularMovieList;
    }

    @Override
    public void deliverResult(List<PopularMovie> data) {
        sStoredResult = data;
        super.deliverResult(data);
    }
}

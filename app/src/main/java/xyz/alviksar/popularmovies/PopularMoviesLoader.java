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
 * AsyncTaskLoader for the popular movies
 */

public class PopularMoviesLoader extends AsyncTaskLoader<List<PopularMovie>> {

    private static final String TAG = PopularMoviesLoader.class.getSimpleName();

    // mJson will store the raw JSON result
    private static List<PopularMovie> sStoredResult;
    // Current sort criteria
    private static String sSort = "";

    private Boolean sortChanged;

    public PopularMoviesLoader(Context context, Bundle args, float posterWidthInches) {
        super(context);
      //  mSort = PopularMoviesPreferences.getSort(context);
        String sort = args.getString(context.getResources().getString(R.string.pref_sort_key));
        sortChanged = !sSort.equals(sort);
        if (sortChanged) {
            sSort = sort;
        }
        TheMovieDbHttpUtils.init(context, posterWidthInches);
    }

    @Override
    protected void onStartLoading() {
        // If we have already cached valid results, just deliver them.
        if (sStoredResult != null && !sortChanged ) {
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

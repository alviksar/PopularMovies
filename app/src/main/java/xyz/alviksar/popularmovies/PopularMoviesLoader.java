package xyz.alviksar.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.List;

import xyz.alviksar.popularmovies.model.PopularMovie;
import xyz.alviksar.popularmovies.utils.TheMovieDbHttpUtils;
import xyz.alviksar.popularmovies.utils.TheMovieDbJsonUtils;

/**
 * Created by Alexey on 23.02.2018.
 */

public class PopularMoviesLoader extends AsyncTaskLoader<List<PopularMovie>> {

    private static final String TAG = PopularMoviesLoader.class.getSimpleName();

    // mJson will store the raw JSON result
    private List<PopularMovie> mStoredResult;

    public PopularMoviesLoader(Context context, final Bundle args) {
        super(context);
        TheMovieDbHttpUtils.setAPI_KEY(context.getResources().getString(R.string.themoviedb_v3_key));
    }

    @Override
    protected void onStartLoading() {

        // If no arguments were passed, we don't have a query to perform
//        if (args == null) {
//            return;
//        }

        // If we have already cached results, just deliver them.
        // If we don't have any cached results, force a load.
        if (mStoredResult != null) {
            deliverResult(mStoredResult);
        } else {
           forceLoad();
        }
    }

    @Override
    public List<PopularMovie> loadInBackground() {

//                        /* Extract the search query from the args using our constant */
//        String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);
//
//                /* If the user didn't enter anything, there's nothing to search for */
//        if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
//            return null;
//        }

        List<PopularMovie> popularMovieList = null;
        try {
            popularMovieList = TheMovieDbJsonUtils.getMoviesFromJson(TheMovieDbHttpUtils.getMoviesByPopularity());
        } catch (Exception e) {
            Log.e(TAG, "No response received", e);
        }
        return popularMovieList;
    }

    @Override
    public void deliverResult(List<PopularMovie> data) {
        mStoredResult = data;
        super.deliverResult(data);
    }
}

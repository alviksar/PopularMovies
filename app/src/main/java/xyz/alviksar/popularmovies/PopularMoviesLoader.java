package xyz.alviksar.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import xyz.alviksar.popularmovies.model.PopularMovie;
import xyz.alviksar.popularmovies.utils.TheMovieDbHttpUtils;
import xyz.alviksar.popularmovies.utils.TheMovieDbJsonUtils;

/**
 * Created by Alexey on 23.02.2018.
 */

public class PopularMoviesLoader extends AsyncTaskLoader<List<PopularMovie>> {

    private static final String TAG = PopularMoviesLoader.class.getSimpleName();

    public PopularMoviesLoader(Context context) {
        super(context);
        TheMovieDbHttpUtils.setAPI_KEY(context.getResources().getString(R.string.themoviedb_v3_key));
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<PopularMovie> loadInBackground() {
        List<PopularMovie> popularMovieList = null;
        try {
            popularMovieList = TheMovieDbJsonUtils.getMoviesFromJson(TheMovieDbHttpUtils.getMoviesByPopularity());
        } catch (Exception e) {
            Log.e(TAG, "No response received", e);
        }
        return popularMovieList;
    }
}

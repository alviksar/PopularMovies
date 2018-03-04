package xyz.alviksar.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Defines table and column names for the movie database.
 */

public class PopularMoviesContract {
    /**
     * The name for the movies content provider.
     */
    public static final String CONTENT_AUTHORITY = "xyz.alviksar.popularmovies";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which app will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * The path for looking at movie data from themoviedb.org
     */
    public static final String PATH_THEMOVIEDB = "themoviedb";
    /**
     * The path for looking at movie data from the favorite movie database
     */
    public static final String PATH_FAVORITE_MOVIE = "my_favorite_movie";
    /**
     * The top rated endpoint
     */
    private static final String TOP_RATED_ENDPOINT = "top_rated";
    /**
     * The popular endpoint.
     */
    private static final String POPULAR_ENDPOINT = "popular";

    public static final class FavoriteMoviesEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the FavoriteMovies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE)
                .build();
    }

}

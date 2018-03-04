package xyz.alviksar.popularmovies.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.util.List;

import xyz.alviksar.popularmovies.model.PopularMovie;

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

    public static final class MoviesEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE)
                .build();

        private final static String[] columnNames = {

        };

        public static Cursor fromList(List<PopularMovie> movieList) {

            MatrixCursor cursor = new MatrixCursor(columnNames);

                /*
                String[] columns = {
   BaseColumns._ID,
   SearchManager.SUGGEST_COLUMN_TEXT_1,
   SearchManager.SUGGEST_COLUMN_INTENT_DATA
};

MatrixCursor cursor = new MatrixCursor(columns);

for (int i = 0; i < arr.length(); i++)
{
  String[] tmp = {Integer.toString(i), arr.getString(i), arr.getString(i)};
  cursor.addRow(tmp);
}
return cursor;
                 */
                return cursor;
        }
    }

}

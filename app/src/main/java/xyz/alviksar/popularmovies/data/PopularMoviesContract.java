package xyz.alviksar.popularmovies.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

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
    public static final String TOP_RATED_ENDPOINT = "top_rated";
    /**
     * The popular endpoint.
     */
    public static final String POPULAR_ENDPOINT = "popular";

    public static final class MoviesEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINALTITLE = "originalTitle";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_PLOTSYNOPSIS = "plotSynopsis";
        public static final String COLUMN_USERRATING = "userRating";
        public static final String COLUMN_POPULARITY = "popularity)";
        public static final String COLUMN_RELEASEDATE = "releaseDate";

        private final static String[] columnNames = {
                BaseColumns._ID,
                COLUMN_ORIGINALTITLE,
                COLUMN_TITLE,
                COLUMN_POSTER,
                COLUMN_PLOTSYNOPSIS,
                COLUMN_USERRATING,
                COLUMN_POPULARITY,
                COLUMN_RELEASEDATE
        };

        public static Cursor fromList(List<PopularMovie> movies) {
            MatrixCursor cursor = new MatrixCursor(columnNames);
            for (int i = 0; i < movies.size(); i++) {
                cursor.addRow(new Object[]{
                        movies.get(i).getId(),
                        movies.get(i).getOriginalTitle(),
                        movies.get(i).getTitle(),
                        movies.get(i).getPoster(),
                        movies.get(i).getPlotSynopsis(),
                        Double.parseDouble(movies.get(i).getUserRating()),
                        Double.parseDouble(movies.get(i).getPopularity()),
                        movies.get(i).getReleaseDate()});
            }
            return cursor;
        }

        public static PopularMovie buildFromCursor(Cursor cursor) {

            return new PopularMovie(
                    // id
                    cursor.getInt(cursor.getColumnIndexOrThrow(MoviesEntry._ID)),
                    // title
                    cursor.getString(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_TITLE)),
                    // originalTitle
                    cursor.getString(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_ORIGINALTITLE)),
                    // poster
                    cursor.getString(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_POSTER)),
                    // plotSynopsis
                    cursor.getString(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_PLOTSYNOPSIS)),
                    // userRating
                    cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_USERRATING)),
                    // popularity
                    cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_POPULARITY)),
                    // releaseDate
                    cursor.getString(cursor.getColumnIndexOrThrow(MoviesEntry.COLUMN_RELEASEDATE))
            );
        }
    }

}

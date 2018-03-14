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
    public static final String FAVORITE_MOVIE_ENDPOINT = "my_favorites";
    /**
     * The top rated endpoint
     */
    public static final String TOP_RATED_ENDPOINT = "top_rated";
    /**
     * The popular endpoint.
     */
    public static final String POPULAR_ENDPOINT = "popular";
    /**
     * The path for looking at trailers
     */
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";


    // The image directory to save the favorite movie posters
    public static final String IMAGE_DIR = "posters";

    /**
     * Table to store favorite movies
     */
    public static final class MoviesEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVORITE_MOVIE_ENDPOINT)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_ORIGINALTITLE = "OriginalTitle";
        public static final String COLUMN_POSTER = "Poster";
        public static final String COLUMN_PLOTSYNOPSIS = "PlotSynopsis";
        public static final String COLUMN_USERRATING = "UserRating";
        public static final String COLUMN_POPULARITY = "Popularity";
        public static final String COLUMN_RELEASEDATE = "ReleaseDate";

        private final static String[] columnNames = {
                BaseColumns._ID,
                COLUMN_TITLE,
                COLUMN_ORIGINALTITLE,
                COLUMN_POSTER,
                COLUMN_PLOTSYNOPSIS,
                COLUMN_USERRATING,
                COLUMN_POPULARITY,
                COLUMN_RELEASEDATE
        };

        /**
         * Makes cursor from list of movies.
         *
         * @param movies List of PopularMovie objects.
         * @return Cursor of movie records.
         */
        public static Cursor fromList(List<PopularMovie> movies) {
            MatrixCursor cursor = new MatrixCursor(columnNames);
            for (int i = 0; i < movies.size(); i++) {
                cursor.addRow(new Object[]{
                        movies.get(i).getId(),
                        movies.get(i).getTitle(),
                        movies.get(i).getOriginalTitle(),
                        movies.get(i).getPoster(),
                        movies.get(i).getPlotSynopsis(),
                        Double.parseDouble(movies.get(i).getUserRating()),
                        Double.parseDouble(movies.get(i).getPopularity()),
                        movies.get(i).getReleaseDate()});
            }
            return cursor;
        }

        /**
         * Builds a PopularMovie object from a cursor on current position.
         *
         * @param cursor MoviesEntry cursor.
         * @return PopularMovie object.
         */
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

    /**
     * The trailer data structure.
     */
    public static final class TrailersEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILERS)
                .build();

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_SITE = "site";

    }

    /**
     * The review data structure.
     */
    public static final class ReviewsEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();

        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

    }
}

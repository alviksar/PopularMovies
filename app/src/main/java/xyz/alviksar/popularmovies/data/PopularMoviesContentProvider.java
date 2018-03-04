package xyz.alviksar.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import xyz.alviksar.popularmovies.PopularMoviesLoader;

/**
 * This ContentProvider provides movie data.
 */

public class PopularMoviesContentProvider extends ContentProvider {

    private static final String TAG = PopularMoviesContentProvider.class.getSimpleName();

    /**
     * These constant will be used to match URIs with the data they are looking for.
     */
    public static final int MATCH_THEMOVIEDB = 100;
    public static final int MATCH_THEMOVIEDB_BY_ID = 101;

    public static final int MATCH_FAVORITE = 200;
    public static final int MATCH_FAVORITE_BY_ID = 201;

    /*
     * The URI Matcher used by this content provider.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Creates the UriMatcher that will match each URI to constants defined above.
     *
     * @return A UriMatcher that correctly matches the constants.
     */
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PopularMoviesContract.PATH_THEMOVIEDB + "/*", MATCH_THEMOVIEDB);
        matcher.addURI(authority, PopularMoviesContract.PATH_THEMOVIEDB + "/#", MATCH_THEMOVIEDB_BY_ID);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITE_MOVIE + "/*", MATCH_FAVORITE);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITE_MOVIE + "/#", MATCH_FAVORITE_BY_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {

            case MATCH_THEMOVIEDB: {
                // Code for querying with a date

                break;
            }
            case MATCH_FAVORITE: {
                // Code for querying with a date
                break;
            }
            case MATCH_THEMOVIEDB_BY_ID:
            case   MATCH_FAVORITE_BY_ID:
                {
                // Code for querying the movie by id
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

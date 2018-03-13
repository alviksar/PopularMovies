package xyz.alviksar.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import xyz.alviksar.popularmovies.MainActivity;
import xyz.alviksar.popularmovies.utils.TheMovieDbHttpUtils;
import xyz.alviksar.popularmovies.utils.TheMovieDbJsonUtils;

/**
 * This ContentProvider provides movie data from themoviedb.org or from a local SQLite database.
 */

public class MoviesProvider extends ContentProvider {

    private static final String TAG = MoviesProvider.class.getSimpleName();

    /**
     * These constant will be used to match URIs with the data they are looking for.
     */
    public static final int MATCH_THEMOVIEDB = 100;

    public static final int MATCH_FAVORITE = 200;
    public static final int MATCH_FAVORITE_BY_ID = 201;

    public static final int MATCH_TRAILERS_BY_ID = 800;
    public static final int MATCH_REVIEWS_BY_ID = 900;

    /*
     * The URI Matcher used by this content provider.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    MovieDBHelper mMovieDbHelper;

    /**
     * Creates the UriMatcher that will match each URI to constants defined above.
     *
     * @return A UriMatcher that correctly matches the constants.
     */
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PopularMoviesContract.PATH_THEMOVIEDB + "/*", MATCH_THEMOVIEDB);
//        matcher.addURI(authority, PopularMoviesContract.PATH_THEMOVIEDB + "/#", MATCH_THEMOVIEDB_BY_ID);
        matcher.addURI(authority, PopularMoviesContract.FAVORITE_MOVIE_ENDPOINT + "", MATCH_FAVORITE);
        //PopularMoviesContract.MoviesEntry.CONTENT_URI
        matcher.addURI(authority, PopularMoviesContract.FAVORITE_MOVIE_ENDPOINT + "/#", MATCH_FAVORITE_BY_ID);

        matcher.addURI(authority, PopularMoviesContract.PATH_TRAILERS + "/#", MATCH_TRAILERS_BY_ID);
        matcher.addURI(authority, PopularMoviesContract.PATH_REVIEWS + "/#", MATCH_REVIEWS_BY_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {

            case MATCH_THEMOVIEDB: {
                // Code for querying with a date from themoviedb.org
                String endpoint = uri.getLastPathSegment();
                TheMovieDbHttpUtils.init(getContext(), MainActivity.POSTER_WIDTH_INCHES);
                try {
                    cursor = PopularMoviesContract.MoviesEntry.fromList(
                            TheMovieDbJsonUtils.getMoviesFromJson(
                                    TheMovieDbHttpUtils.getPopularMoviesByEndPoint(endpoint)
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, "URI: " + uri.toString());
                }
                break;
            }
            case MATCH_FAVORITE: {
                // Select all favorites
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        PopularMoviesContract.MoviesEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        PopularMoviesContract.MoviesEntry._ID);
                break;
            }
            case MATCH_FAVORITE_BY_ID: {
                // Select a single row given by the ID in the URI
                selection = PopularMoviesContract.MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        PopularMoviesContract.MoviesEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case MATCH_TRAILERS_BY_ID: {
                try {
                    String movie_id = uri.getLastPathSegment();
                    cursor = TheMovieDbJsonUtils.getTrailersFromJson(
                            TheMovieDbHttpUtils.getTrailersByMovieId(movie_id)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, "URI: " + uri.toString());
                }
                break;
            }
            case MATCH_REVIEWS_BY_ID: {
                try {
                    String movie_id = uri.getLastPathSegment();
                    cursor = TheMovieDbJsonUtils.getReviewsFromJson(
                            TheMovieDbHttpUtils.getReviewsByMovieId(movie_id)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, "URI: " + uri.toString());
                }
                break;
            }
            default:
                Log.e(TAG, "Unknown uri: " + uri.toString());
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
        throw new RuntimeException("The getType method is not implemented.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Long newRowId = -1L;
        switch (sUriMatcher.match(uri)) {
            case MATCH_FAVORITE:
                newRowId = db.insert(PopularMoviesContract.MoviesEntry.TABLE_NAME, null, values);
                break;
            default:
                Log.e(TAG, "Unknown uri: " + uri.toString());
                break;
        }
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (newRowId == -1) {
            Log.e(TAG, "Wrong uri: " + uri.toString());
            throw new UnsupportedOperationException("Failed to insert row for " + uri);
        }
        else {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        int numRowsDeleted = 0;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH_FAVORITE_BY_ID:
                // Delete a single row given by the ID in the URI
                selection = PopularMoviesContract.MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                numRowsDeleted = database.delete(PopularMoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                Log.e(TAG, "Unknown uri: " + uri.toString());
                throw new UnsupportedOperationException("Deletion is not supported for " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("The update method is not implemented.");
    }
}

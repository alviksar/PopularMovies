package xyz.alviksar.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xyz.alviksar.popularmovies.data.PopularMoviesContract.MoviesEntry;

/**
 * Manages a local database for favorite movies.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    /**
     * The database name.
     */
    private static final String DATABASE_NAME = "my_favorite_movies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                        MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                        MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_ORIGINALTITLE + " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_PLOTSYNOPSIS + " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_USERRATING + " REAL NULL, " +
                        MoviesEntry.COLUMN_POPULARITY + " REAL NULL, " +
                        MoviesEntry.COLUMN_RELEASEDATE + " TEXT NULL); " ;

        // Create the movie db
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

}


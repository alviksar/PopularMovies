package xyz.alviksar.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 * An adapter for a list view that uses a cursor of trailers or a cursor of reviews as its data source.
 */

public class ExtraInfoAdapter extends android.widget.CursorAdapter {

    private static final String TAG = ExtraInfoAdapter.class.getSimpleName();
    /*
     * Types of  showing information.
     */
    public static final int SHOW_TRAILERS = 0;
    public static final int SHOW_REVIEWS = 1;

    // The type of info:  trailers or reviews
    private int mMode;

        public ExtraInfoAdapter(Context context, Cursor c) {
            super(context, c, 0 /* flags */);
        }

        /**
         * Makes a new blank list item view. No data is set (or bound) to the views yet.
         *
         * @param context app context
         * @param cursor  The cursor from which to get the data. The cursor is already
         *                moved to the correct position.
         * @param parent  The parent to which the new view is attached to
         * @return the newly created list item view.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(
                    R.layout.detail_list_item, parent, false);
        }

        /**
         * This method binds the data (in the current row pointed to by cursor) to the given
         * list item layout.
         *
         * @param view    Existing view, returned earlier by newView() method
         * @param context app context
         * @param cursor  The cursor from which to get the data. The cursor is already moved to the
         *                correct row.
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvName = view.findViewById(R.id.top_text_view);
            TextView tvType = view.findViewById(R.id.bottom_text_view);
            String name, type;
            // Extract properties from cursor
            if (mMode == SHOW_TRAILERS) {
                view.findViewById(R.id.play_button_image).setVisibility(View.VISIBLE);
                name = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.TrailersEntry.COLUMN_NAME));
                type = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.TrailersEntry.COLUMN_TYPE));
            } else if (mMode == SHOW_REVIEWS) {
                view.findViewById(R.id.play_button_image).setVisibility(View.GONE);
                name = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.ReviewsEntry.COLUMN_AUTHOR));
                type = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.ReviewsEntry.COLUMN_CONTENT));
            } else {
                name = "";
                type = "";
                Log.e(TAG, "Unknown type of info" );

            }
            // Populate fields with extracted properties
            tvName.setText(name);
            tvType.setText(type);
        }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mMode) {
        this.mMode = mMode;
    }
}

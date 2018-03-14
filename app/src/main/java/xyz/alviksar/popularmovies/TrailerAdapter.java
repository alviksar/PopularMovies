package xyz.alviksar.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 * An adapter for a list view that uses a cursor of trailers as its data source.
 */
public class TrailerAdapter extends android.widget.CursorAdapter {

    public TrailerAdapter(Context context, Cursor c) {
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
        TextView tvName = (TextView) view.findViewById(R.id.top_text_view);
        TextView tvType = (TextView) view.findViewById(R.id.bottom_text_view);
        // Extract properties from cursor
        String name_trailer = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.TrailersEntry.COLUMN_NAME));
        String type_trailer = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.TrailersEntry.COLUMN_TYPE));
        // Populate fields with extracted properties
        tvName.setText(name_trailer);
        tvType.setText(type_trailer);
    }
}

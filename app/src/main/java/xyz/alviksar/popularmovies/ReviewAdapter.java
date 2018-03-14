package xyz.alviksar.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 * An adapter for a list view that uses a cursor of reviews as its data source.
 */

public class ReviewAdapter extends android.widget.CursorAdapter {

    public ReviewAdapter(Context context, Cursor c) {
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
        return LayoutInflater.from(context).inflate(R.layout.detail_list_item, parent, false);
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
        ImageView imageView = view.findViewById(R.id.play_button_image);
        imageView.setVisibility(View.GONE);
        // Extract properties from cursor
        String name_trailer = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.ReviewsEntry.COLUMN_AUTHOR));
        String type_trailer = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.ReviewsEntry.COLUMN_CONTENT));
        // Populate fields with extracted properties
        tvName.setText(name_trailer);
        tvType.setText(type_trailer);
    }
}

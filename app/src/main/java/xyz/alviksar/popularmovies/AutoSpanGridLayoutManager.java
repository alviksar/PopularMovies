package xyz.alviksar.popularmovies;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Thanks to Diep Nguyen fot the idea
 * https://codentrick.com/part-4-android-recyclerview-grid/
 */

public class AutoSpanGridLayoutManager extends GridLayoutManager {
    private int mColumnWidthPixels;
    private boolean mColumnWidthChanged = true;

    public AutoSpanGridLayoutManager(Context context, int columnWidthInches) {
        super(context, 1);
        if (columnWidthInches > 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            mColumnWidthPixels = Math.round(columnWidthInches * metrics.xdpi);
            mColumnWidthChanged = true;
        }
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mColumnWidthChanged && mColumnWidthPixels > 0) {
            int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / mColumnWidthPixels);
            setSpanCount(spanCount);
            mColumnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}
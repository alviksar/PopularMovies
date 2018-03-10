package xyz.alviksar.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 * Created by Alexey on 10.03.2018.
 */

public class TrailerClickListener implements ListView.OnItemClickListener {
    private Context mContext;

    public TrailerClickListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        Toast.makeText(mContext,
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                PopularMoviesContract.TrailersEntry.COLUMN_NAME)),
                Toast.LENGTH_LONG).show();
    }
}

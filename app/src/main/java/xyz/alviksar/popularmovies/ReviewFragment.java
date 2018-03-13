package xyz.alviksar.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 * This Fragment shows a review list in DetailActivity
 */

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mMovieId;
    private static final int REVIEW_LOADER = 9;
    private ReviewAdapter mReviewAdapter;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.extra_info_list, container, false);

        // Find the ListView which will be populated with the trailers
        ListView listView = (ListView)rootView.findViewById(R.id.extra_list);

        mReviewAdapter = new ReviewAdapter(rootView.getContext(), null);
        listView.setAdapter(mReviewAdapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String url = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.ReviewsEntry.COLUMN_URL));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Provide the package with the movie ID to the loader
        Bundle bundle = getArguments();
        getLoaderManager().initLoader(REVIEW_LOADER, bundle, this);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String movieId = bundle.getString(getString(R.string.movie_id_key));
        return new CursorLoader(getContext(),
                PopularMoviesContract.ReviewsEntry
                        .CONTENT_URI.buildUpon()
                        .appendPath(movieId).build(),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mReviewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mReviewAdapter.swapCursor(null);
    }

}

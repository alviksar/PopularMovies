package xyz.alviksar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 * This Fragment shows a movie trailer or review list in DetailActivity.
 */

public class ExtraInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ExtraInfoFragment.class.getSimpleName();

    private static final int TRAILERS_LOADER = 8;
    private static final int REVIEW_LOADER = 9;

    private ExtraInfoAdapter mExtraInfoAdapter;

    public ExtraInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        // Check network connection
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            rootView = inflater.inflate(R.layout.extra_info_list, container, false);

            // Find the ListView which will be populated with the trailers
            ListView listView = rootView.findViewById(R.id.extra_list);

            mExtraInfoAdapter = new ExtraInfoAdapter(rootView.getContext(), null);
            listView.setAdapter(mExtraInfoAdapter);

            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                    Uri uri = null;
                    switch (mExtraInfoAdapter.getMode()) {
                        case ExtraInfoAdapter.SHOW_TRAILERS:
                            final String YOUTUBEWATCH = "https://www.youtube.com/watch";
                            String site = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.TrailersEntry.COLUMN_SITE));
                            String key = cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.TrailersEntry.COLUMN_KEY));
                            if ("YouTube".equals(site)) {
                                uri = Uri.parse(YOUTUBEWATCH).buildUpon()
                                        .appendQueryParameter("v", key).build();
                                // Sample:
                                // "https://www.youtube.com/watch?v=MS7GN2Lgdas")
                            }
                            break;
                        case ExtraInfoAdapter.SHOW_REVIEWS:
                            uri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.ReviewsEntry.COLUMN_URL)));

                            break;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

            // Provide the package with the movie ID to the loader
            Bundle bundle = getArguments();
            if (bundle != null && bundle.containsKey(getString(R.string.extra_info_type))) {
                int mode = bundle.getInt(getString(R.string.extra_info_type));
                mExtraInfoAdapter.setMode(mode);
                if (mode == ExtraInfoAdapter.SHOW_TRAILERS) {
                    getLoaderManager().initLoader(TRAILERS_LOADER, bundle, this);
                } else if (mode == ExtraInfoAdapter.SHOW_REVIEWS) {
                    getLoaderManager().initLoader(REVIEW_LOADER, bundle, this);
                }
            } else {
                Log.e(TAG, "There is no type of info for the fragment.");
            }

        } else {
            rootView = inflater.inflate(R.layout.extra_info_error, container, false);
        }
        return rootView;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String movieId = bundle.getString(getString(R.string.movie_id_key));
        int mode = bundle.getInt(getString(R.string.extra_info_type));
        switch (i) {
            case TRAILERS_LOADER:
                return new CursorLoader(getContext(),
                        PopularMoviesContract.TrailersEntry
                                .CONTENT_URI.buildUpon()
                                .appendPath(movieId).build(),
                        null, null, null, null);
            case REVIEW_LOADER:
                return new CursorLoader(getContext(),
                        PopularMoviesContract.ReviewsEntry
                                .CONTENT_URI.buildUpon()
                                .appendPath(movieId).build(),
                        null, null, null, null);
            default:
                throw new RuntimeException("The type of info is unknown.");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mExtraInfoAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mExtraInfoAdapter.swapCursor(null);
    }
}

package xyz.alviksar.popularmovies;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;

/**
 *
 */

public class TrailerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TRAILERS_LOADER = 800;
    private TrailerAdapter mTrailerAdapter;

    public TrailerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.extra_info_list, container, false);


        // Find the ListView which will be populated with the trailers
        ListView listView = (ListView) rootView.findViewById(R.id.extra_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
//        View emptyView = rootView.findViewById(R.id.empty_view);
//        listView.setEmptyView(emptyView);

        mTrailerAdapter = new TrailerAdapter(rootView.getContext(), null);
        listView.setAdapter(mTrailerAdapter);

        getLoaderManager().initLoader(TRAILERS_LOADER, null, this);
        // Create a list of words
//            final ArrayList<Word> words = new ArrayList<Word>();
//            words.add(new Word("Where are you going?", "minto wuksus",
//                    R.raw.phrase_where_are_you_going));
//            words.add(new Word("What is your name?", "tinnә oyaase'nә",
//                    R.raw.phrase_what_is_your_name));
//            words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
//            words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
//            words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
//            words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
//            words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
//            words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
//            words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
//            words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));
//
//            // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
//            // adapter knows how to create list items for each item in the list.
//            WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);
//
//            // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
//            // There should be a {@link ListView} with the view ID called list, which is declared in the
//            // word_list.xml file.
//            ListView listView = (ListView) rootView.findViewById(R.id.extra_list);
//
//            // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
//            // {@link ListView} will display list items for each {@link Word} in the list.
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(new WordClickListener(getActivity()));

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this.getContext(),
                PopularMoviesContract.TrailersEntry.CONTENT_URI.buildUpon().appendPath("19404").build(),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mTrailerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mTrailerAdapter.swapCursor(null);
    }
}

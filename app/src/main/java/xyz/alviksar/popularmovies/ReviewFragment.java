package xyz.alviksar.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */

public class ReviewFragment extends Fragment {

        public ReviewFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.test_layout, container, false);

/*
            // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
            // adapter knows how to create list items for each item in the list.
            WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

            // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
            // There should be a {@link ListView} with the view ID called list, which is declared in the
            // word_list.xml file.
            ListView listView = (ListView) rootView.findViewById(R.id.list);

            // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
            // {@link ListView} will display list items for each {@link Word} in the list.
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new WordClickListener(getActivity()));
*/
            return rootView;
        }


        @Override
        public void onStop() {
            super.onStop();
        }
}

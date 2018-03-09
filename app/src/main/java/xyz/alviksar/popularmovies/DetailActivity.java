package xyz.alviksar.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;
import xyz.alviksar.popularmovies.databinding.DetailLayoutBinding;
import xyz.alviksar.popularmovies.model.PopularMovie;

/**
 * Displays a screen with additional information
 */
public class DetailActivity extends AppCompatActivity {

    private PopularMovie mMovie;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getString(R.string.movie_parcel_key), mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_layout);



        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_parcel_key))) {
            mMovie = getIntent().getParcelableExtra(getString(R.string.movie_parcel_key));
        }
        else {
            mMovie = savedInstanceState.getParcelable(getString(R.string.movie_parcel_key));
        }

        setTitle(mMovie.getTitle());

        // Uses dataBinding
       DetailActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.detail_activity);
        DetailPagerAdapter adapter = new DetailPagerAdapter(this, getSupportFragmentManager());
       binding.setPagerAdapter(adapter);
       binding.slidingTabs.setupWithViewPager(binding.viewpager);
       binding.setPopularMovie(mMovie);
/*
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Create an adapter that knows which fragment should be shown on each page
        DetailPagerAdapter adapter = new DetailPagerAdapter(this, getSupportFragmentManager());
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
*/

    }
}

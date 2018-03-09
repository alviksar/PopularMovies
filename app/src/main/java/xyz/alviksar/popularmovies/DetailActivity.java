package xyz.alviksar.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;
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
      //  setContentView(R.layout.detail_activity);

        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_parcel_key))) {
            mMovie = getIntent().getParcelableExtra(getString(R.string.movie_parcel_key));
        } else {
            mMovie = savedInstanceState.getParcelable(getString(R.string.movie_parcel_key));
        }

        setTitle(mMovie.getTitle());

        // Uses dataBinding
        DetailActivityBinding binding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.detail_activity);
        DetailPagerAdapter adapter = new DetailPagerAdapter(this, getSupportFragmentManager());
        binding.setPagerAdapter(adapter);
        binding.slidingTabs.setupWithViewPager(binding.viewpager);
        binding.setPopularMovie(mMovie);

    }
}

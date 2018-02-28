package xyz.alviksar.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;
import xyz.alviksar.popularmovies.model.PopularMovie;

public class DetailActivity extends AppCompatActivity {



  //  private ImageView mPoster;
//    private TextView mTitle;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable(getString(R.string.movie_parcel_key), mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        PopularMovie movie;
//        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_parcel_key))) {
            movie = getIntent().getParcelableExtra(getString(R.string.movie_parcel_key));
//        }
//        else {
//           mMovie = savedInstanceState.getParcelable(getString(R.string.movie_parcel_key));
//        }
        setTitle(movie.getTitle());
        DetailActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.detail_activity);
        binding.setPopularMovie(movie);
    }
}

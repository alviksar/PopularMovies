package xyz.alviksar.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import xyz.alviksar.popularmovies.model.PopularMovie;

public class DetailActivity extends AppCompatActivity {

    private PopularMovie mMovie;

    private ImageView mPoster;
    private TextView mTitle;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable(getString(R.string.movie_parcel_key), mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri imageUri = intent.getData();

//        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_parcel_key))) {
            mMovie = intent.getParcelableExtra(getString(R.string.movie_parcel_key));
//        }
//        else {
//           mMovie = savedInstanceState.getParcelable(getString(R.string.movie_parcel_key));
//        }

        setTitle(mMovie.getTitle());
        setContentView(R.layout.activity_detail);
        mPoster = findViewById(R.id.im_poster);

        mTitle = findViewById(R.id.tv_original_title);
        if (mMovie != null) {
            mTitle.setText(mMovie.getOriginalTitle());
        }





        Picasso.with(this)
                .load(imageUri)
                //     .placeholder(R.drawable.user_placeholder)
                //      .error(R.drawable.user_placeholder_error)
                .into(mPoster);
    }
}

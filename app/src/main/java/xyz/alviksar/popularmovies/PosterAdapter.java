package xyz.alviksar.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import xyz.alviksar.popularmovies.model.Movie;

/**
 * Exposes a grid of the movie posters to RecyclerView
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mMovieList;

    /**
     * Creates a ForecastAdapter.
     *
     * @param context Used to talk to the UI and app resources
     */
    public PosterAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.poster_list_item, parent, false);
        view.setFocusable(true);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(R.drawable.interstellar)
          //      .placeholder(R.drawable.user_placeholder)
          //      .error(R.drawable.user_placeholder_error)
                .into(holder.posterView);
    }

    @Override
    public int getItemCount() {
        return 20;
        /*
        if (mMovieList != null) {
            return mMovieList.size();
        } else {
            return 0;
        }
        */
    }

    /**
     * Swaps the date used by the PosterAdapter for its movie data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the movie data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param data the new data to use as PosterAdapter's data source
     */
    void swapData(ArrayList<Movie> data) {
        mMovieList = data;
        notifyDataSetChanged();
    }

    class PosterAdapterViewHolder extends RecyclerView.ViewHolder {

        final ImageView posterView;

        public PosterAdapterViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.iv_poster);
        }
    }

}

package xyz.alviksar.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import xyz.alviksar.popularmovies.data.PopularMoviesContract;
import xyz.alviksar.popularmovies.model.PopularMovie;
import xyz.alviksar.popularmovies.utils.TheMovieDbHttpUtils;

/**
 * Exposes a grid of the movie posters to RecyclerView
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {
    /**
     * The interface to handle clicks on items within this Adapter
     */
    public interface PosterAdapterOnClickHandler {
        void onClick(PopularMovie movie);
    }

    private final  Context mContext;

    private Cursor mCursor;

    /**
     * The member that receives onClick messages
     */
    final private PosterAdapterOnClickHandler mClickHandler;


    /**
     * Creates a PosterAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler Used to assign onClick receiver
     */
    public PosterAdapter(@NonNull Context context, PosterAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PosterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.poster_list_item, parent, false);
        view.setFocusable(true);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterAdapterViewHolder holder, int position) {
        if (mCursor == null || mCursor.getCount() == 0) return;
        mCursor.moveToPosition(position);
        int columnIndex = mCursor.getColumnIndex(
                PopularMoviesContract.MoviesEntry.COLUMN_POSTER);
        String poster = mCursor.getString(columnIndex);
        String fullPathToPoster = TheMovieDbHttpUtils.getFullPathToPoster(poster);
        Picasso.with(mContext)
                .load(fullPathToPoster)
                //     .placeholder(R.drawable.user_placeholder)
                //      .error(R.drawable.user_placeholder_error)
                .into(holder.posterView);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Swaps the date used by the PosterAdapter for its movie data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the movie data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param cursor the new cursor to use as PosterAdapter's data source
     */
    void swapData(Cursor cursor) {
        mCursor = cursor;
        // After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView posterView;

        public PosterAdapterViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.iv_poster);

            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the movie id that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * movie id.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mCursor != null) {
                mCursor.moveToPosition(position);
                PopularMovie movie = PopularMoviesContract.MoviesEntry.buildFromCursor(mCursor);
//                int movieId = mCursor.getInt(mCursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));
                mClickHandler.onClick(movie);
            }
        }
    }

}

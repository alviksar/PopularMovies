package xyz.alviksar.popularmovies;

import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.Context;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class DetailPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    private String mMoveId;


    /**
     * Context of the app
     */
    private Context mContext;

    /**
     * Create a new {@link DetailPagerAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public DetailPagerAdapter(Context context, String movieId, FragmentManager fm) {
        super(fm);
        mContext = context;
        mMoveId = movieId;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            TrailerFragment trailerFragment= new TrailerFragment();
            Bundle bundle = new Bundle(1);
            bundle.putString(mContext.getString(R.string.movie_id_key), mMoveId);
            trailerFragment.setArguments(bundle);
            return trailerFragment;
        } else {
            return new ReviewFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.TrailersHeader);

        } else {
            return mContext.getString(R.string.ReviewsHeader);
        }
    }


    /**
     * Add this method into databinding for setting PagerAdapter
     *
    https://codedesignpattern.wordpress.com/2016/09/16/view-pager-with-databinding-custom-setter/
     */
    @BindingAdapter(value = {"android:pagerAdapter"}, requireAll = false)
    public static void setViewPager(ViewPager viewPager, FragmentPagerAdapter adapter) {
        viewPager.setAdapter(adapter);

    }

}


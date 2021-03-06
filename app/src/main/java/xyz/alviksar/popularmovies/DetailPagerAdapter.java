package xyz.alviksar.popularmovies;

import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.Context;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Provides an appropriate fragment for a view pager.
 */
public class DetailPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    private static final int PAGE_COUNT = 2;

    // The move ID of DetailActivity
    private final String mMoveId;

    /**
     * Creates a new object.
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
        Fragment fragment;
        int mode;
        if (position == 1) {
            mode = ExtraInfoAdapter.SHOW_REVIEWS;
        } else {
            mode = ExtraInfoAdapter.SHOW_TRAILERS;
        }
        fragment = new ExtraInfoFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(mContext.getString(R.string.movie_id_key), mMoveId);
        bundle.putInt(mContext.getString(R.string.extra_info_type), mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return mContext.getString(R.string.reviews_header);
        } else {
            return mContext.getString(R.string.trailers_header);
        }
    }


    /**
     * Sets PagerAdapter into databinding
     * <p>
     * https://codedesignpattern.wordpress.com/2016/09/16/view-pager-with-databinding-custom-setter/
     */
    @BindingAdapter(value = {"android:pagerAdapter"}, requireAll = false)
    public static void setViewPager(ViewPager viewPager, FragmentPagerAdapter adapter) {
        viewPager.setAdapter(adapter);

    }

}


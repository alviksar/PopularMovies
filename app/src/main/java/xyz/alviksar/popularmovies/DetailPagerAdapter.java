package xyz.alviksar.popularmovies;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.Context;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.alviksar.popularmovies.databinding.DetailActivityBinding;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class DetailPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;


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
    public DetailPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//      //  return super.instantiateItem(container, position);
//        DetailActivityBinding layout =
//       DataBindingUtil.inflate(
//                LayoutInflater.from(mContext), R.layout.test_layout, container, false);
//// Set your databinding up here
//        container.addView(layout.getRoot());
//        return layout.getRoot();
//    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ReviewFragment();
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
            return "Trailers";

        } else {
            return "Reviews";
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


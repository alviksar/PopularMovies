package xyz.alviksar.popularmovies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Adapter for trailer and review pages
 */

public class ExtraInfoPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;


    public ExtraInfoPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Trailers";
        } else
            return "Reviews";
    }

}

package xyz.alviksar.popularmovies;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/*
* Solves the problem that the "wrap_content" doesn't seem to be working with the ViewPager.
* http://life-optimized.blogspot.ru/2014/05/how-to-force-view-pager-to-wrap-its.html
* https://stackoverflow.com/questions/8394681/android-i-am-unable-to-have-viewpager-wrap-content
*/

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs){

        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();

            // Add a ListView multiplier
            int n = 1;
            ListView listView = child.findViewById(R.id.extra_list);
            if (listView != null) {
                n = listView.getCount();
            }
            if(h*n > height) height = h*n;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }
}
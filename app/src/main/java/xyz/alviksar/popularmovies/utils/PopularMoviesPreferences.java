package xyz.alviksar.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import xyz.alviksar.popularmovies.R;

public class PopularMoviesPreferences {
    /**
     * Helper method to handle setting the sort criteria in Preferences
     *
     * @param context  Context used to get the SharedPreferences
     * @param sort     the sorting of movies
     */
    public static void setSort(Context context, String sort) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_sort_key), sort);
        editor.apply();
    }


    /**
     * Returns the sort criteria currently set in Preferences
     *
     * @param context Context used to access SharedPreferences
     * @return  The sort criteria that current user has set in SharedPreferences or default value.
     */
    public static String getSort(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }

}

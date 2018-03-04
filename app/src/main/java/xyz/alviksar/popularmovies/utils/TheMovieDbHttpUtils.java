package xyz.alviksar.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import xyz.alviksar.popularmovies.R;

    /*
    The class to fetch popular movies from themoviedb.org.
    In order to request popular movies you will want to request data from
    the /movie/popular and /movie/top_rated endpoints. An API Key is required.

    http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
    */

public class TheMovieDbHttpUtils {

    private static final String TAG = TheMovieDbHttpUtils.class.getSimpleName();

    // The base URL for a list
    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie";
    // The top rated endpoint
    public static final String TOP_RATED_ENDPOINT = "top_rated";
    // The popular endpoint
    public static final String POPULAR_ENDPOINT = "popular";
    // API KEY parameter
    private static final String API_KEY_PARAM = "api_key";
    private static String api_key_value = "";

    // The base URL for image
    private static final String MOVIEDB_IMAGE_URL = "http://image.tmdb.org/t/p";
    // The image width endpoint
    private static String image_width_endpoint = "w342";

    private static String sort_by_popularity;
    private static String sort_by_rating;

    /**
     * Initializes the global parameters
     */
    public static void init(Context context, float posterSizeInches) {
        sort_by_popularity = context.getString(R.string.sort_by_most_popular);
        sort_by_rating = context.getString(R.string.sort_by_top_rated);
        api_key_value = context.getResources().getString(R.string.themoviedb_v3_key);

        // Set width for images
        image_width_endpoint = choosePosterWidth(context, posterSizeInches);
    }

    /**
     * Returns a json response by sort criteria
     *
     * @param sort The type of sorting movies
     * @return The json response from the movie db server
     */
    public static String getMoviesBy(String sort) throws IOException {
        if (sort.equals(sort_by_popularity)) {
            return getResponseFromHttpUrl(buildTheMovieDbUrl(POPULAR_ENDPOINT));
        } else {
            if (sort.equals(sort_by_rating)) {
                return getResponseFromHttpUrl(buildTheMovieDbUrl(TOP_RATED_ENDPOINT));
            } else {
                return "";
            }
        }
    }

    public static String getPopularMoviesByEndPoint(String endPoint) throws IOException {
        return getResponseFromHttpUrl(buildTheMovieDbUrl(endPoint));
    }

    /**
     * Builds the URL used to talk to the movie themoviedb server
     *
     * @param sort The type of sorting movies
     * @return The Url to use to query the themoviedb server
     */
    @Nullable
    private static URL buildTheMovieDbUrl(String sort) {
        Uri moviesQueryUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(sort)
                .appendQueryParameter(API_KEY_PARAM, api_key_value)
                .build();

        try {
            URL moviesQueryUrl = new URL(moviesQueryUri.toString());
            Log.v(TAG, "URL: " + moviesQueryUri);
            return moviesQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the entire result from the HTTP response
     *
     * @param url The URL to fetch the HTTP response from
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Build the complete url you will need to fetch the image.
     */
    public static String getFullPathToPoster(String imageFileName) {
        return Uri.parse(MOVIEDB_IMAGE_URL).buildUpon()
                .appendPath(image_width_endpoint)
                .appendPath(imageFileName)
                //     .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build().toString();
    }

    /**
     * Chooses the width image depending on the screen resolution
     *
     * @param context          App context
     * @param posterSizeInches The image width in inches
     * @return The string for URL to image at the themoviedb  server
     */
    private static String choosePosterWidth(Context context, float posterSizeInches) {
        /*
        A poster width can be one of the following:
        "w92", "w154", "w185", "w342", "w500", "w780", or "original".
       */
        final String[] posterWidth = {"w92", "w154", "w185", "w342", "w500", "w780"};
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        for (int i = 0; i < posterWidth.length - 1; i++) {
            if (metrics.xdpi * posterSizeInches <= Float.parseFloat(posterWidth[i].substring(1)))
                return posterWidth[i];
        }
        return "original";
    }
}



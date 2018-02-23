package xyz.alviksar.popularmovies.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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
    private static final String TOP_RATED_ENDPOINT = "top_rated";
    // The popular endpoint
    private static final String POPULAR_ENDPOINT = "popular";
    // API KEY parameter
    private static final String API_KEY_PARAM = "api_key";
    private static String API_KEY_VALUE = "";

    // The base URL for image
    private static final String MOVIEDB_IMAGE_URL = "http://image.tmdb.org/t/p";
    // The image width endpoint
    private static final String IMAGE_WIDTH_ENDPOINT = "w342";

    public static void setAPI_KEY(String key) {
        API_KEY_VALUE = key;
    }

    public static String getMoviesByPopularity() throws IOException {
        return getResponseFromHttpUrl(buildTheMovieDbUrl(TOP_RATED_ENDPOINT));
    }

    public static String getMoviesByRating() throws IOException {
        return getResponseFromHttpUrl(buildTheMovieDbUrl(POPULAR_ENDPOINT));
    }

    /**
     * Builds the URL used to talk to the movie db server
     *
     * @param queryType The type of sorting movies
     * @return The Url to use to query the movie db server
     */
    @Nullable
    private static URL buildTheMovieDbUrl(String queryType) {
        Uri moviesQueryUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(queryType)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
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
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
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
/*
Build the complete url you will need to fetch the image.

The base URL will look like: http://image.tmdb.org/t/p/.
Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original".
For most phones we recommend using “w185”.
And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”
Combining these three parts gives us a final url of http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
 */
    public static String getFullPathToPoster(String imageFileName) {
        return Uri.parse(MOVIEDB_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_WIDTH_ENDPOINT)
                .appendPath(imageFileName)
           //     .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build().toString();
    }
}



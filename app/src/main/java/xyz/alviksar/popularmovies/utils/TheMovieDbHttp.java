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

public class TheMovieDbHttp {

    private static final String TAG = TheMovieDbHttp.class.getSimpleName();

    // The base URL
    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3";
    // The top rated endpoint
    private static final String TOP_RATED_ENDPOINT = "/movie/top_rated";
    // The popular endpoint
    private static final String POPULAR_ENDPOINT = "/movie/popular";
    // API KEY parameter
    private static final String API_KEY_PARAM = "api_key";
    private static String API_KEY_VALUE = "";

    public static void setAPI_KEY(String key) {
        API_KEY_VALUE = key;
    }

    public String getMoviesByPopularity() throws IOException {
        return getResponseFromHttpUrl(buildTheMovieDbUrl(TOP_RATED_ENDPOINT));
    }

    public String getMoviesByRating() throws IOException {
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
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
}

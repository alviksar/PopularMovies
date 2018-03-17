package xyz.alviksar.popularmovies.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
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
    /*
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir(PopularMoviesContract.IMAGE_DIR, Context.MODE_PRIVATE);
     */
    // The directory name for saving posters locally
    private static File sDirectory;

    private static final String TAG = TheMovieDbHttpUtils.class.getSimpleName();

    // The base URL for a list
    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie";
    // API KEY parameter
    private static final String API_KEY_PARAM = "api_key";
    private static String api_key_value = "";

    // The base URL for image
    private static final String MOVIEDB_IMAGE_URL = "http://image.tmdb.org/t/p";
    // The image width endpoint
    private static String image_width_endpoint = "w342";

    private static final String TRAILERS_ENDPOINT = "videos";
    private static final String REVIEWS_ENDPOINT = "reviews";

    /**
     * Initializes the global parameters.
     */
    public static void init(File dir, float posterSizeInches,
                            DisplayMetrics metrics, String apiKey) {
        sDirectory = dir;
        api_key_value = apiKey;

        // Set width for posters
        // DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        image_width_endpoint = choosePosterWidth(metrics, posterSizeInches);
    }


    public static String getPopularMoviesByEndPoint(String endPoint) throws IOException {
        return getResponseFromHttpUrl(buildTheMovieDbUrl(endPoint));
    }

    /**
     * Builds the URL used to talk to the movie themoviedb server.
     *
     * @param sort The type of sorting movies.
     * @return The Url to use to query the themoviedb server.
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
     * Returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response.
     * @throws IOException Related to network and stream reading.
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
    public static String getFullPathToPoster(String poster) {
        // If there is a local file take it

        File file = new File(sDirectory, poster);
        if (file.exists()) {
            return Uri.fromFile(file).toString();
        } else {
            return Uri.parse(MOVIEDB_IMAGE_URL).buildUpon()
                    .appendPath(image_width_endpoint)
                    .appendPath(poster)
                    //     .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                    .build().toString();
        }
    }

    /**
     * Chooses the width image depending on the screen resolution.
     *
     * @param metrics          The display metrics.
     * @param posterSizeInches The image width in inches.
     * @return The string for URL to image at the themoviedb  server.
     */
    private static String choosePosterWidth(DisplayMetrics metrics, float posterSizeInches) {
        /*
        A poster width can be one of the following:
        "w92", "w154", "w185", "w342", "w500", "w780", or "original".
       */
        final String[] posterWidth = {"w92", "w154", "w185", "w342", "w500", "w780"};

        for (int i = 0; i < posterWidth.length - 1; i++) {
            if (metrics.xdpi * posterSizeInches <= Float.parseFloat(posterWidth[i].substring(1)))
                return posterWidth[i];
        }
        return "original";
    }

    /**
     * Returns a trailers by movie_id.
     *
     * @param movie_id The movie id.
     * @return The json response from the movie db server.
     */
    @Nullable
    public static String getTrailersByMovieId(String movie_id) {
        /*
        * URL sample:
        * https://api.themoviedb.org/3/movie/19404/videos?api_key=<>
        */
        Uri moviesQueryUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movie_id)
                .appendPath(TRAILERS_ENDPOINT)
                .appendQueryParameter(API_KEY_PARAM, api_key_value)
                .build();
        try {
            URL trailersQueryUrl = new URL(moviesQueryUri.toString());
            Log.v(TAG, "URL: " + moviesQueryUri);
            return getResponseFromHttpUrl(trailersQueryUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a reviews by movie_id.
     *
     * @param movie_id The movie id.
     * @return The json response from the movie db server.
     */
    @Nullable
    public static String getReviewsByMovieId(String movie_id) {
        Uri moviesQueryUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movie_id)
                .appendPath(REVIEWS_ENDPOINT)
                .appendQueryParameter(API_KEY_PARAM, api_key_value)
                .build();
        try {
            URL trailersQueryUrl = new URL(moviesQueryUri.toString());
            Log.v(TAG, "URL: " + moviesQueryUri);
            return getResponseFromHttpUrl(trailersQueryUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}



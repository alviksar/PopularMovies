package xyz.alviksar.popularmovies.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xyz.alviksar.popularmovies.model.PopularMovie;

/*
 * Sample:
 * {
 * "page":1,
 * "total_results":19615,
 * "total_pages":981,
 * "results":[
 * {
 * "vote_count":119,
 * "id":441614,
 * "video":false,
 * "vote_average":4.7,
 * "title":"Loving",
 * "popularity":568.476218,
 * "poster_path":"\/6uOMVZ6oG00xjq0KQiExRBw2s3P.jpg",
 * "original_language":"es",
 * "original_title":"Amar",
 * "genre_ids":[
 * 10749
 * ],
 * "backdrop_path":"\/iBM6zvlOmcfodGhUa36sy7pM2Er.jpg",
 * "adult":false,
 * "overview":"Laura and Carlos love each other as if every day was the last, and perhaps that first love intensity is what will tear them apart a year later.",
 * "release_date":"2017-04-21"
 * },
 * {
 * We need:
 * id
 * original title
 * movie poster image
 * a plot synopsis (called overview in the api)
 * user rating (called vote_average in the api)
 * release date
 */

/**
 * Parses a JSON string to movie objects
 */

public final class TheMovieDbJsonUtils {

    // The array of movie records
    private static final String TMD_RESULTS = "results";
    // The record id
    private static final String TMD_ID = "id";
    // The  title
    private static final String TMD_TITLE = "title";
    // The original title
    private static final String TMD_ORIGINAL_TITLE = "original_title";
    // The path to the poster image
    private static final String TMD_POSTER_PATH = "poster_path";
    // The plot synopsis
    private static final String TMD_OVERVIEW = "overview";
    // The user rating
    private static final String TMD_VOTE_AVERAGE = "vote_average";
    // The release date
    private static final String TMD_RELEASE_DATE = "release_date";

    /**
     * This method parses JSON from a web response and returns a list of movie objects
     *
     * @param jsonStr JSON response from server
     * @return a list of movie objects
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Nullable
    public static List<PopularMovie> getMoviesFromJson(String jsonStr)
            throws JSONException {

        JSONObject forecastJson = new JSONObject(jsonStr);

        if (!forecastJson.has(TMD_RESULTS)) {
            // Something went wrong
            return null;
        }

        JSONArray jsonMoviesArray = forecastJson.getJSONArray(TMD_RESULTS);

        ArrayList<PopularMovie> popularMovieList = new ArrayList<>(jsonMoviesArray.length());
        PopularMovie popularMovie;

        for (int i = 0; i < jsonMoviesArray.length(); i++) {

            // Get the JSON object representing the popularMovie record
            JSONObject jsonMovieObject = jsonMoviesArray.getJSONObject(i);
            // Create a movie object
            popularMovie = new PopularMovie();
            popularMovie.setId(jsonMovieObject.getInt(TMD_ID));
            popularMovie.setTitle(jsonMovieObject.optString(TMD_TITLE));
            popularMovie.setOriginalTitle(jsonMovieObject.optString(TMD_ORIGINAL_TITLE));
            popularMovie.setPlotSynopsis(jsonMovieObject.optString(TMD_OVERVIEW));
            String poster = jsonMovieObject.optString(TMD_POSTER_PATH);
            poster = TextUtils.substring(poster, 1, poster.length());
            popularMovie.setPoster(poster);
            popularMovie.setReleaseDate(jsonMovieObject.optString(TMD_RELEASE_DATE));
            popularMovie.setUserRating(jsonMovieObject.getDouble(TMD_VOTE_AVERAGE));
            // Add a movie object to the list
            popularMovieList.add(popularMovie);
        }
        return popularMovieList;

    }

}

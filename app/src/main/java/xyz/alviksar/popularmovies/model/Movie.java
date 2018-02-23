package xyz.alviksar.popularmovies.model;

import java.util.Date;

/**
 * Represents movie data
 */

public class Movie {
    // movie record id
    private int id;
    // original title
    private String originalTitle;
    // movie poster image file name
    private String posterImage;
    // A plot synopsis (called overview in the api)
    private String plotSynopsis;
    // A plot synopsis (called overview in the api)
    private Double userRating;
    // release date
    private Date releaseDate;

    public Movie() {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }


}
/*
      {
         "vote_count":119,
         "id":441614,
         "video":false,
         "vote_average":4.7,
         "title":"Loving",
         "popularity":568.476218,
         "poster_path":"\/6uOMVZ6oG00xjq0KQiExRBw2s3P.jpg",
         "original_language":"es",
         "original_title":"Amar",
         "genre_ids":[
            10749
         ],
         "backdrop_path":"\/iBM6zvlOmcfodGhUa36sy7pM2Er.jpg",
         "adult":false,
         "overview":"Laura and Carlos love each other as if every day was the last, and perhaps that first love intensity is what will tear them apart a year later.",
         "release_date":"2017-04-21"
      },
 */
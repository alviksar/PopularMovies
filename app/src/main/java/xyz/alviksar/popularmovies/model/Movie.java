package xyz.alviksar.popularmovies.model;

import java.util.Date;

/**
 * Created by Alexey on 21.02.2018.
 */

public class Movie {
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

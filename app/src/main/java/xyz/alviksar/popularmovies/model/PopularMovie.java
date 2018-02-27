package xyz.alviksar.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents movie data
 */

public class PopularMovie implements Parcelable {
    // movie record id
    private int id;
    // title
    private String title;
    // original title
    private String originalTitle;
    // movie poster image file name
    private String poster;
    // A plot synopsis (called overview in the api)
    private String plotSynopsis;
    // A plot synopsis (called overview in the api)
    private Double userRating;
    // release date
    private String releaseDate;

    public PopularMovie() {
    }

    // Constructor from Parcel
    private PopularMovie(Parcel in){
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        poster = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    public static final Creator<PopularMovie> CREATOR = new Creator<PopularMovie>() {
        @Override
        public PopularMovie createFromParcel(Parcel in) {
            return new PopularMovie(in);
        }

        @Override
        public PopularMovie[] newArray(int size) {
            return new PopularMovie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(poster);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
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
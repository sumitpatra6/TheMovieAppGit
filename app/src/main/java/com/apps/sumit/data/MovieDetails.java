package com.apps.sumit.data;

import android.graphics.Bitmap;

/**
 * Created by Sumit on 9/19/2016.
 */
public class MovieDetails {

    private String movieName;
    private String releaseDate;
    private String voteAverage;
    private String synopsys;
    private Bitmap mIcon;

    public MovieDetails(String movieName, String releaseDate, String voteAverage, String synopsys, Bitmap mIcon){
        this.movieName = movieName;
        this.releaseDate = movieName;
        this.voteAverage = voteAverage;
        this.synopsys = synopsys;
        this.mIcon = mIcon;
    }

    public Bitmap getmIcon(){
        return this.mIcon;
    }
    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsys() {
        return synopsys;
    }
}

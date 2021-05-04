package com.e.easytech.response;

import com.e.easytech.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// this class is for getting multiple movies(Movies movie)-popular movies
public class MoviesearchResponse {
    @SerializedName("total_results")
    @Expose()
    private int total_count;

    @SerializedName("results")
    @Expose()
    private List<MovieModel> movies;

    public int getTotal_count(){
        return total_count;
    }

    public List<MovieModel> getMovies(){
        return movies;
    }

    @Override
    public String  toString() {
        return "MoviesearchResponse{" +
                "total_count=" + total_count +
                ", movies=" + movies +
                '}';
    }
}

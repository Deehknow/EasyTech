package com.e.easytech.utils;

import com.e.easytech.models.MovieModel;
import com.e.easytech.response.MoviesearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    // search for movie
    @GET("/3/search/movie")
    Call<MoviesearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page
    );

    // https://api.themoviedb.org/3/movie/550?api_key=7416254b7cecbf25691f71f27ab7ee7c

    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
        @Path("movie_id") int movie_id,
                @Query("api_key") String api_key);

    //get popular movies
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher

    @GET("/3/movie/popular")
    Call<MoviesearchResponse> getPopular(
        @Query("api_key") String key,
                @Query("page") int page
    );


}

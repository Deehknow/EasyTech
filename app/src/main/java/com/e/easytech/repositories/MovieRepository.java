package com.e.easytech.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.easytech.models.MovieModel;
import com.e.easytech.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static  MovieRepository instance;
    private MovieApiClient mMovieApiClient;
    private  String mQuery;
    private  int mPageNumber;

    public static MovieRepository getInstance(){
        if (instance==null){
            instance= new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        mMovieApiClient= MovieApiClient.getInstance();

    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovieApiClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPopular(){
        return mMovieApiClient.getMoviesPopular();
    }

    // calling the method
    public void searchMovieApi(String query, int pageNumber){

        mQuery=query;
        mPageNumber=pageNumber;
        mMovieApiClient.searchMoviesApi(query,pageNumber);
    }

    public void searchMoviePopular(int pageNumber){
        mPageNumber=pageNumber;
        mMovieApiClient.searchMoviesPopular(pageNumber);
    }
    public void searchNextPage(){
        searchMovieApi(mQuery,mPageNumber+1);
    }

}

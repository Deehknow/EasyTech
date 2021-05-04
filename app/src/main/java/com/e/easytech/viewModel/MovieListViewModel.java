package com.e.easytech.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.easytech.models.MovieModel;
import com.e.easytech.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    // THIS CLASS IS FOR VIEW MODEL

    //
    private MovieRepository movieRepository;

    //CONSTRUCTOR
    public MovieListViewModel(){
    movieRepository=MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPopular(){
        return movieRepository.getPopular();
    }

    // calling the method


    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void searchMoviePopular(int pageNumber){
        movieRepository.searchMoviePopular(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
}

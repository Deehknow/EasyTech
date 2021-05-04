package com.e.easytech.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.easytech.AppExecutors;
import com.e.easytech.models.MovieModel;
import com.e.easytech.response.MoviesearchResponse;
import com.e.easytech.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    //LIVE DATA FOR SEARCH
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    // making global RUNNABLE request
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    // LIVE DATA FOR POPULAR MOVIES
    private MutableLiveData<List<MovieModel>> mMoviePopular;

    //POPULAR MOVIES RUNNABLE
    private RetrieveMoviesRunnablePopular retrieveMoviesRunnablePopular;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {

        mMovies = new MutableLiveData<>();
        mMoviePopular=new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPopular() {
        return mMoviePopular;
    }

    public void searchMoviesApi(String query, int pageNumber) {

        if (retrieveMoviesRunnable!= null){
            retrieveMoviesRunnable=null;
        }

        retrieveMoviesRunnable =new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // cancelling retrofit call
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }
    public void searchMoviesPopular(int pageNumber) {

        if (retrieveMoviesRunnablePopular!= null){
            retrieveMoviesRunnablePopular=null;
        }

        retrieveMoviesRunnablePopular =new RetrieveMoviesRunnablePopular(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePopular);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // cancelling retrofit call
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    // retrieving data from RESTAPI by runnable class
    // we have 2 queries
    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest = false;

        public RetrieveMoviesRunnable(String query, int pageNumber){
            this.query=query;
            this.pageNumber=pageNumber;
            this.cancelRequest=cancelRequest;
        }

        @Override
        public void run() {
            // getting the response objects
            try {
                Response response=getMovies(query,pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code()==200){
                    List<MovieModel> list=new ArrayList<>(((MoviesearchResponse)response.body()).getMovies());
                    if (pageNumber==1){
                        //sending value to live data
                        //postValue(); used for background thread
                        //setValue: not for background thread
                        mMovies.postValue(list);
                    }
                    else {
                        List<MovieModel> currentMovies=mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }
                else {
                    String error=response.errorBody().string();
                    Log.v("tag","Error" +error);
                    mMovies.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

            // search query/method
            private Call<MoviesearchResponse> getMovies (String query,int pageNumber){
                return Servicey.getMovieApi().searchMovie(
                        Credentials.API_KEY,
                        query,
                        String.valueOf(pageNumber)
                );
            }

            private void cancelRequest(){
                Log.v("Tag","Cancelling Search Request");
                cancelRequest=true;
            }



    }
    private class RetrieveMoviesRunnablePopular implements Runnable {

        private int pageNumber;
        boolean cancelRequest = false;

        public RetrieveMoviesRunnablePopular(int pageNumber){
            this.pageNumber=pageNumber;
            this.cancelRequest=cancelRequest;
        }

        @Override
        public void run() {
            // getting the response objects
            try {
                Response response2=getPopular(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response2.code()==200){
                    List<MovieModel> list=new ArrayList<>(((MoviesearchResponse)response2.body()).getMovies());
                    if (pageNumber==1){
                        //sending value to live data
                        //postValue(); used for background thread
                        //setValue: not for background thread
                        mMoviePopular.postValue(list);
                    }
                    else {
                        List<MovieModel> currentMovies=mMoviePopular.getValue();
                        currentMovies.addAll(list);
                        mMoviePopular.postValue(currentMovies);
                    }
                }
                else {
                    String error=response2.errorBody().string();
                    Log.v("tag","Error" +error);
                    mMoviePopular.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMoviePopular.postValue(null);
            }


        }

        // search query/method
        private Call<MoviesearchResponse> getPopular (int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,

                    pageNumber
            );
        }

        private void cancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest=true;
        }



    }
}



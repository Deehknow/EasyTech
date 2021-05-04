package com.e.easytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.e.easytech.adapters.MovieRecyclerView;
import com.e.easytech.adapters.OnMovieListener;
import com.e.easytech.models.MovieModel;
import com.e.easytech.request.Servicey;
import com.e.easytech.response.MoviesearchResponse;
import com.e.easytech.utils.Credentials;
import com.e.easytech.utils.MovieApi;
import com.e.easytech.viewModel.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMovieListener {
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;
    //VIEWMODEL
    private MovieListViewModel movieListViewModel;


    boolean isPopular=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupSearchView();
        recyclerView = findViewById(R.id.recycler_view);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigRecyclerView();
        observePopularMovies();
        ObserveAnyChange();


        // getting popular movie data
        movieListViewModel.searchMoviePopular(1);

        //Recyclerview Pagination // loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (! recyclerView.canScrollVertically(1)){
                    // display the next search results on the next page of api
                    movieListViewModel.searchNextPage();
                }

            }
        });


    }

    private void observePopularMovies() {
        movieListViewModel.getPopular().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // observing any data change
                if (movieModels != null) {
                    for (MovieModel movieModel : movieModels) {
                        // getting the data in log
                        Log.v("tag", "onChanged:" + movieModel.getTitle());
                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    private void setupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular=false;
            }
        });

    }

    //OBSERVING DATA CHANGE

    private void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // observing any data change
                if (movieModels != null) {
                    for (MovieModel movieModel : movieModels) {
                        // getting the data in log
                        Log.v("tag", "onChanged:" + movieModel.getTitle());
                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }
/*
    //calling method in main activity
    // calling the method
    public void searchMovieApi(String query, int pageNumber){
        movieListViewModel.searchMovieApi(query,pageNumber);
    }

 */

/*
    private void GetRetrofitResponse() {
        MovieApi movieApi= Servicey.getMovieApi();
        Call<MoviesearchResponse> responseCall=movieApi
                .searchMovie(
                        Credentials.API_KEY,
                        "Action",
                        "1"
                );
        responseCall.enqueue(new Callback<MoviesearchResponse>() {
            @Override
            public void onResponse(Call<MoviesearchResponse> call, Response<MoviesearchResponse> response) {
                if (response.code() == 200){
                //    Log.v("tag","the response" + response.body().toString());
                    List<MovieModel> movies=new ArrayList<>(response.body().getMovies());
                    for (MovieModel movie: movies){
                        Log.v("tag","name" + movie.getTitle() );
                    }
                }
                else {

                    try {
                        Log.v("tag", "error" + response.errorBody().toString());} catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesearchResponse> call, Throwable t) {

            }
        });

    }

    private void GetRetrofitResponseAccordingToID(){
        MovieApi movieApi=Servicey.getMovieApi();
        Call<MovieModel> responseCall=movieApi
                .getMovie(343611,
                        Credentials.API_KEY);
        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code()==200){
                    MovieModel movie=response.body();
                    Log.v("tag","the response " +movie.getTitle());
                } else{
                    try {
                        Log.v("tag","error" +response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    */

    // intializing recyclerview and adding data to it
    private void ConfigRecyclerView() {
        //livedata cannot be passed through constructor
        movieRecyclerViewAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public void onMovieClick(int position) {
        //  Toast.makeText(this,"the position" +position, Toast.LENGTH_SHORT).show();
        // we need movie id to get all its details;
        Intent intent=new Intent(this,MovieDetails.class);
        intent.putExtra("movie",movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }


}
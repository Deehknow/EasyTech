package com.e.easytech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e.easytech.models.MovieModel;

public class MovieDetails extends AppCompatActivity {
//widgets
    private ImageView imageViewDetails;
    private TextView titledetails,description;
    private RatingBar ratingBarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewDetails=findViewById(R.id.imageView_details);
        titledetails=findViewById(R.id.movie_details_title);
        description=findViewById(R.id.movie_detail_overview);
        ratingBarDetails=findViewById(R.id.movie_details_rating_bar);

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")){
            MovieModel movieModel=getIntent().getParcelableExtra("movie");
         //   Log.v("tag","incoming intent"+movieModel.getTitle());

            titledetails.setText(movieModel.getTitle());
            description.setText(movieModel.getMovie_overview());
            ratingBarDetails.setRating((movieModel.getVote_average())/2);
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" +movieModel.getPoster_path())
                    .into(imageViewDetails);

        }
    }
}
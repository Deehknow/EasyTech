package com.e.easytech.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.easytech.R;
import com.e.easytech.models.MovieModel;
import com.e.easytech.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;
    private static final int DISPLAY_POPULAR=1;
    public static final int DISPLAY_SEARCH=2;


    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if (viewType==DISPLAY_SEARCH){
          View view= LayoutInflater.from(parent.getContext()).inflate(
                  R.layout.movie_list_item,parent,false
          );
          return new MoviewViewHolder(view,onMovieListener);
      }
      else {
          View view= LayoutInflater.from(parent.getContext()).inflate(
                  R.layout.movie_list_item,parent,false
          );
          return new PopularMovieViewHolder(view,onMovieListener);
      }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       int itemViewType= getItemViewType(position);
       if (itemViewType == DISPLAY_SEARCH){
           ((MoviewViewHolder)holder).title.setText(mMovies.get(position).getTitle());
           ((MoviewViewHolder)holder).release_date.setText(mMovies.get(position).getRelease_date());
           ((MoviewViewHolder)holder).duration.setText(mMovies.get(position).getOriginal_language());
           ((MoviewViewHolder)holder).ratingBar.setRating((mMovies.get(position).getVote_average())/2);
           //IMAGE VIEW USING Glide Library
           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500" +mMovies.get(position).getPoster_path())
                   .into(((MoviewViewHolder)holder).imageView);
       }
       else{
           ((PopularMovieViewHolder)holder).title.setText(mMovies.get(position).getTitle());
           ((PopularMovieViewHolder)holder).release_date.setText(mMovies.get(position).getRelease_date());
           ((PopularMovieViewHolder)holder).duration.setText(mMovies.get(position).getOriginal_language());
           ((PopularMovieViewHolder)holder).ratingBar.setRating((mMovies.get(position).getVote_average())/2);
           //IMAGE VIEW USING Glide Library
           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500" +mMovies.get(position).getPoster_path())
                   .into(((PopularMovieViewHolder)holder).imageView);
       }

    }

    @Override
    public int getItemCount() {
        if (mMovies != null){
        return mMovies.size();
        }
        return 0;

    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR){
            return DISPLAY_POPULAR;
        }
        else
            return DISPLAY_SEARCH;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    // getting the id of the selected movie
    public MovieModel getSelectedMovie(int position){
        if (mMovies != null){
            if (mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }
}

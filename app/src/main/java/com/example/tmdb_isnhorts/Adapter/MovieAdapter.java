package com.example.tmdb_isnhorts.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.Result;
import com.example.tmdb_isnhorts.View.MovieActivity;
import com.example.tmdb_isnhorts.databinding.MovieListItemBinding;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyHolder> {

    MovieListItemBinding movieListItemBinding;
    Context context;
    ArrayList<Result> resultArrayList;

    public MovieAdapter(Context context, ArrayList<Result> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        movieListItemBinding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.movie_list_item, parent, false);

        return new MyHolder(movieListItemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Result result = resultArrayList.get(position);

        holder.movieListItemBinding.setResult(result);

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        MovieListItemBinding movieListItemBinding;

        public MyHolder(MovieListItemBinding movieListItemBinding) {
            super(movieListItemBinding.getRoot());
            this.movieListItemBinding = movieListItemBinding;


            movieListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (position!=RecyclerView.NO_POSITION) {

                        Result result = resultArrayList.get(position);


                        Intent intent = new Intent(context, MovieActivity.class);
                        intent.putExtra("id", result.getId().toString());
                        context.startActivity(intent);


                    }
                }
            });


        }




    }
}

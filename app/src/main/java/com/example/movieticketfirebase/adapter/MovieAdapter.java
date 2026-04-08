package com.example.movieticketfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieticketfirebase.databinding.ItemMovieBinding;
import com.example.movieticketfirebase.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface OnMovieClickListener {
        void onBook(Movie movie);
    }

    private final Context context;
    private final OnMovieClickListener listener;
    private final List<Movie> movies = new ArrayList<>();

    public MovieAdapter(Context context, OnMovieClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void submitList(List<Movie> list) {
        movies.clear();
        movies.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.binding.txtTitle.setText(movie.getTitle());
        holder.binding.txtGenre.setText("Thể loại: " + safe(movie.getGenre()));
        holder.binding.txtDuration.setText("Thời lượng: " + (movie.getDuration() == null ? 0 : movie.getDuration()) + " phút");
        holder.binding.txtDescription.setText(safe(movie.getDescription()));

        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            Glide.with(context).load(movie.getPosterUrl()).into(holder.binding.imgPoster);
        }

        holder.binding.btnBook.setOnClickListener(v -> listener.onBook(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ItemMovieBinding binding;

        public MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

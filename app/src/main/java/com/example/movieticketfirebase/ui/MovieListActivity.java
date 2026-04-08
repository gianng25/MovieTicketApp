package com.example.movieticketfirebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieticketfirebase.adapter.MovieAdapter;
import com.example.movieticketfirebase.databinding.ActivityMovieListBinding;
import com.example.movieticketfirebase.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {
    private ActivityMovieListBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        adapter = new MovieAdapter(this, movie -> {
            Intent intent = new Intent(this, ShowtimeListActivity.class);
            intent.putExtra("movieId", movie.getId());
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("posterUrl", movie.getPosterUrl());
            startActivity(intent);
        });

        binding.recyclerMovies.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerMovies.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadMovies);

        binding.btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });

        binding.btnTickets.setOnClickListener(v ->
                startActivity(new Intent(this, TicketListActivity.class)));

        loadMovies();
    }

    private void loadMovies() {
        binding.swipeRefresh.setRefreshing(true);
        db.collection("movies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Movie> movies = new ArrayList<>();
                    queryDocumentSnapshots.getDocuments().forEach(doc -> {
                        Movie movie = doc.toObject(Movie.class);
                        if (movie != null) {
                            movie.setId(doc.getId());
                            movies.add(movie);
                        }
                    });
                    adapter.submitList(movies);
                    binding.swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    binding.swipeRefresh.setRefreshing(false);
                    Toast.makeText(this, "Lỗi tải phim: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

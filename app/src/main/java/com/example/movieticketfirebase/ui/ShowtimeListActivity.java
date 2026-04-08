package com.example.movieticketfirebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieticketfirebase.adapter.ShowtimeAdapter;
import com.example.movieticketfirebase.databinding.ActivityShowtimeListBinding;
import com.example.movieticketfirebase.model.Showtime;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeListActivity extends AppCompatActivity {
    private ActivityShowtimeListBinding binding;
    private FirebaseFirestore db;
    private ShowtimeAdapter adapter;
    private String movieId;
    private String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowtimeListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        binding.txtHeader.setText("Suất chiếu - " + movieTitle);

        adapter = new ShowtimeAdapter(showtime -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("movieId", movieId);
            intent.putExtra("movieTitle", movieTitle);
            intent.putExtra("showtimeId", showtime.getId());
            intent.putExtra("showtimeText", showtime.getStartTimeText());
            intent.putExtra("theaterId", showtime.getTheaterId());
            intent.putExtra("theaterName", showtime.getTheaterName());
            intent.putExtra("price", showtime.getPrice());
            intent.putStringArrayListExtra("bookedSeats",
                    new java.util.ArrayList<>(showtime.getBookedSeats()));
            startActivity(intent);
        });

        binding.recyclerShowtimes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerShowtimes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShowtimes();
    }

    private void loadShowtimes() {
        db.collection("showtimes")
                .whereEqualTo("movieId", movieId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Showtime> list = new ArrayList<>();
                    queryDocumentSnapshots.getDocuments().forEach(doc -> {
                        Showtime showtime = doc.toObject(Showtime.class);
                        if (showtime != null) {
                            showtime.setId(doc.getId());
                            list.add(showtime);
                        }
                    });
                    adapter.submitList(list);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải suất chiếu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

package com.example.movieticketfirebase.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketfirebase.databinding.ActivityBookingBinding;
import com.example.movieticketfirebase.model.Ticket;
import com.example.movieticketfirebase.util.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private ActivityBookingBinding binding;
    private final List<String> allSeats = Arrays.asList(
            "A1","A2","A3","A4",
            "B1","B2","B3","B4",
            "C1","C2","C3","C4"
    );
    private ArrayList<String> bookedSeats;
    private String selectedSeat = null;

    private String movieId, movieTitle, showtimeId, showtimeText, theaterId, theaterName, posterUrl;
    private long price;
    private FirebaseRepository repository;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new FirebaseRepository();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        showtimeId = getIntent().getStringExtra("showtimeId");
        showtimeText = getIntent().getStringExtra("showtimeText");
        theaterId = getIntent().getStringExtra("theaterId");
        theaterName = getIntent().getStringExtra("theaterName");
        posterUrl = getIntent().getStringExtra("posterUrl"); // Nhận posterUrl
        price = getIntent().getLongExtra("price", 0L);
        bookedSeats = getIntent().getStringArrayListExtra("bookedSeats");
        if (bookedSeats == null) bookedSeats = new ArrayList<>();

        binding.txtMovie.setText(movieTitle);
        binding.txtShowtime.setText("Suất chiếu: " + showtimeText);
        binding.txtTheater.setText("Rạp: " + theaterName);

        createSeatButtons();

        binding.btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void createSeatButtons() {
        GridLayout grid = binding.gridSeats;
        grid.removeAllViews();

        for (String seat : allSeats) {
            Button button = new Button(this);
            button.setText(seat);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            button.setLayoutParams(params);

            if (bookedSeats.contains(seat)) {
                button.setEnabled(false);
                button.setBackgroundColor(Color.GRAY);
            } else {
                button.setOnClickListener(v -> {
                    selectedSeat = seat;
                    binding.txtSelectedSeat.setText("Ghế đã chọn: " + seat);
                    // Có thể thêm đổi màu ghế đang chọn ở đây
                });
            }
            grid.addView(button);
        }
    }

    private void confirmBooking() {
        if (currentUser == null) {
            toast("Bạn chưa đăng nhập");
            return;
        }

        if (selectedSeat == null) {
            toast("Vui lòng chọn ghế");
            return;
        }

        Ticket ticket = new Ticket();
        ticket.setUserId(currentUser.getUid());
        ticket.setMovieId(movieId);
        ticket.setMovieTitle(movieTitle);
        ticket.setPosterUrl(posterUrl); // Lưu posterUrl vào ticket
        ticket.setTheaterId(theaterId);
        ticket.setTheaterName(theaterName);
        ticket.setShowtimeId(showtimeId);
        ticket.setShowtimeText(showtimeText);
        ticket.setSeatNumber(selectedSeat);
        ticket.setPrice(price);
        ticket.setStatus("booked");

        repository.createTicket(ticket,
                unused -> repository.updateBookedSeat(showtimeId, selectedSeat,
                        unused2 -> {
                            toast("Đặt vé thành công");
                            finish();
                        },
                        e -> toast("Cập nhật ghế thất bại: " + e.getMessage())),
                e -> toast("Đặt vé thất bại: " + e.getMessage()));
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

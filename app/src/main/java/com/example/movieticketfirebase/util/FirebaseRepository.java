package com.example.movieticketfirebase.util;

import androidx.annotation.NonNull;

import com.example.movieticketfirebase.model.Movie;
import com.example.movieticketfirebase.model.Showtime;
import com.example.movieticketfirebase.model.Theater;
import com.example.movieticketfirebase.model.Ticket;
import com.example.movieticketfirebase.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseFirestore getDb() {
        return db;
    }

    public void saveUserProfile(UserProfile profile,
                                OnSuccessListener<Void> successListener,
                                OnFailureListener failureListener) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", profile.getUid());
        map.put("name", profile.getName());
        map.put("email", profile.getEmail());
        map.put("phone", profile.getPhone());
        map.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users")
                .document(profile.getUid())
                .set(map, SetOptions.merge())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void createTicket(Ticket ticket,
                             OnSuccessListener<Void> successListener,
                             OnFailureListener failureListener) {
        String ticketId = db.collection("tickets").document().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("ticketId", ticketId);
        map.put("userId", ticket.getUserId());
        map.put("movieId", ticket.getMovieId());
        map.put("movieTitle", ticket.getMovieTitle());
        map.put("posterUrl", ticket.getPosterUrl()); // Đã thêm lưu posterUrl
        map.put("theaterId", ticket.getTheaterId());
        map.put("theaterName", ticket.getTheaterName());
        map.put("showtimeId", ticket.getShowtimeId());
        map.put("showtimeText", ticket.getShowtimeText());
        map.put("seatNumber", ticket.getSeatNumber());
        map.put("price", ticket.getPrice());
        map.put("status", "booked");
        map.put("bookingTime", FieldValue.serverTimestamp());

        db.collection("tickets")
                .document(ticketId)
                .set(map)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updateBookedSeat(String showtimeId, String seat,
                                 OnSuccessListener<Void> successListener,
                                 OnFailureListener failureListener) {
        db.collection("showtimes")
                .document(showtimeId)
                .update("bookedSeats", FieldValue.arrayUnion(seat))
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    // Hàm mới: Hủy vé và giải phóng ghế
    public void cancelTicket(String ticketId, String showtimeId, String seat,
                             OnSuccessListener<Void> successListener,
                             OnFailureListener failureListener) {
        db.collection("tickets").document(ticketId).delete()
                .addOnSuccessListener(unused -> {
                    // Xóa ghế khỏi mảng bookedSeats để người khác có thể đặt
                    db.collection("showtimes")
                            .document(showtimeId)
                            .update("bookedSeats", FieldValue.arrayRemove(seat))
                            .addOnSuccessListener(successListener)
                            .addOnFailureListener(failureListener);
                })
                .addOnFailureListener(failureListener);
    }
}

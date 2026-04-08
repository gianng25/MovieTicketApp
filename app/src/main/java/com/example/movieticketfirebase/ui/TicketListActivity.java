package com.example.movieticketfirebase.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieticketfirebase.adapter.TicketAdapter;
import com.example.movieticketfirebase.databinding.ActivityTicketListBinding;
import com.example.movieticketfirebase.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TicketListActivity extends AppCompatActivity {
    private ActivityTicketListBinding binding;
    private TicketAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new TicketAdapter();
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.recyclerTickets.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerTickets.setAdapter(adapter);

        binding.btnBack.setOnClickListener(v -> finish());

        loadTickets();
    }

    private void loadTickets() {
        if (currentUser == null) {
            toast("Bạn chưa đăng nhập");
            return;
        }

        db.collection("tickets")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Ticket> tickets = new ArrayList<>();
                    queryDocumentSnapshots.getDocuments().forEach(doc -> {
                        Ticket ticket = doc.toObject(Ticket.class);
                        if (ticket != null) {
                            ticket.setId(doc.getId());
                            tickets.add(ticket);
                        }
                    });
                    adapter.submitList(tickets);
                })
                .addOnFailureListener(e -> toast("Lỗi tải vé: " + e.getMessage()));
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

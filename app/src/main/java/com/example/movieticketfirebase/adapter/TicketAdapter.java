package com.example.movieticketfirebase.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketfirebase.databinding.ItemTicketBinding;
import com.example.movieticketfirebase.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final List<Ticket> tickets = new ArrayList<>();

    public void submitList(List<Ticket> list) {
        tickets.clear();
        tickets.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTicketBinding binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.binding.txtMovieTitle.setText(ticket.getMovieTitle());
        holder.binding.txtTheater.setText("Rạp: " + ticket.getTheaterName());
        holder.binding.txtShowtime.setText("Suất chiếu: " + ticket.getShowtimeText());
        holder.binding.txtSeat.setText("Ghế: " + ticket.getSeatNumber());
        holder.binding.txtPrice.setText("Giá: " + ticket.getPrice() + " VND");
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        ItemTicketBinding binding;

        public TicketViewHolder(ItemTicketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

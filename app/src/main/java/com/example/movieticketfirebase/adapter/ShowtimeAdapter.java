package com.example.movieticketfirebase.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketfirebase.databinding.ItemShowtimeBinding;
import com.example.movieticketfirebase.model.Showtime;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    public interface OnShowtimeClickListener {
        void onSelect(Showtime showtime);
    }

    private final List<Showtime> showtimes = new ArrayList<>();
    private final OnShowtimeClickListener listener;

    public ShowtimeAdapter(OnShowtimeClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Showtime> list) {
        showtimes.clear();
        showtimes.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShowtimeBinding binding = ItemShowtimeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShowtimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime item = showtimes.get(position);
        holder.binding.txtTheater.setText(item.getTheaterName() == null ? "Rạp chưa cập nhật" : item.getTheaterName());
        holder.binding.txtTime.setText("Giờ chiếu: " + (item.getStartTimeText() == null ? "" : item.getStartTimeText()));
        holder.binding.txtPrice.setText("Giá vé: " + item.getPrice() + " VND");
        holder.binding.btnSelect.setOnClickListener(v -> listener.onSelect(item));
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        ItemShowtimeBinding binding;

        public ShowtimeViewHolder(ItemShowtimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

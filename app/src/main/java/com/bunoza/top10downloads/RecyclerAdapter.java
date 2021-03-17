package com.bunoza.top10downloads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<FeedEntry> applications = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public void setApplications(List<FeedEntry> applications) {
        this.applications.addAll(applications);
    }

    public RecyclerAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder, parent, false);
        return new ViewHolder(cellView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setName(position+1 +". " + applications.get(position).getName());
        holder.setArtist(applications.get(position).getArtist());
        holder.setDate(applications.get(position).getReleaseDate());
        holder.setIcon(applications.get(position).getImageURL());
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }
}

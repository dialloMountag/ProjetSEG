package com.example.projetseg2505;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.BranchViewHolder> {

    // Your existing variables (e.g., List<Location> branches)
    List<Location> locations;

    // Constructor to set up the adapter with data
    public LocationAdapter(List<Location> branches) {
        this.locations = branches;
    }

    // ViewHolder class
    public static class BranchViewHolder extends RecyclerView.ViewHolder {
        // Your UI elements in each item view (e.g., TextViews)
        TextView textViewBranchName;
        TextView textViewWorkingHours;
        TextView textViewLocation;

        public BranchViewHolder(View itemView) {
            super(itemView);
            // Initialize UI elements
            textViewBranchName = itemView.findViewById(R.id.textViewBranchName);
            textViewWorkingHours = itemView.findViewById(R.id.textViewWorkingHours);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);

        }
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_branch, parent, false);
        return new BranchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        // Bind data to the UI elements in each item view
        Location branch = locations.get(position);
        // Set values to TextViews, ImageViews, etc.
        holder.textViewBranchName.setText(branch.getName());
        holder.textViewWorkingHours.setText("Working Hours: " + branch.getOpeningTime() + " - " + branch.getClosingTime());
        holder.textViewLocation.setText("Location: " + branch.getAddress());

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}

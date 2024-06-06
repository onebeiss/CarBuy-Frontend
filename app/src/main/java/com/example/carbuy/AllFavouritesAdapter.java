package com.example.carbuy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllFavouritesAdapter extends RecyclerView.Adapter<AllFavouritesViewHolder> {
    private List<AllFavouritesItem> itemList;
    private Activity activity;
    private AllFavouritesAdapter.OnItemClickListener favsListener;

    public AllFavouritesAdapter(List<AllFavouritesItem> itemList, Activity activity) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AllFavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_favourites, parent, false);
        return new AllFavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllFavouritesViewHolder holder, int position) {
        AllFavouritesItem item = itemList.get(position);
        holder.showData(item, activity);

        final int adapterPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favsListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    favsListener.onItemClick(adapterPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void setOnItemClickListener(AllFavouritesAdapter.OnItemClickListener listener) {
        favsListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

package com.example.carbuy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllAdsAdapter extends RecyclerView.Adapter<AllAdsViewHolder>{
    private List<AllAdsItem> itemList;
    private Activity activity;
    private OnItemClickListener adsListener;

    public AllAdsAdapter(List<AllAdsItem> itemList, Activity activity) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AllAdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_ads, parent, false);
        return new AllAdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllAdsViewHolder holder, int position) {
        AllAdsItem item = itemList.get(position);
        holder.showData(item, activity);

        final int adapterPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adsListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    adsListener.onItemClick(adapterPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        adsListener = listener;
        }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

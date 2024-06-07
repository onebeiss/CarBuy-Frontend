package com.example.carbuy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    private List<SearchResultItem> itemList;
    private Activity activity;
    private SearchResultAdapter.OnItemClickListener resultListener;

    public SearchResultAdapter(List<SearchResultItem> itemList, Activity activity) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        SearchResultItem item = itemList.get(position);
        holder.showData(item, activity);

        final int adapterPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    resultListener.onItemClick(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(SearchResultAdapter.OnItemClickListener listener) {
        resultListener = listener;
    }
}

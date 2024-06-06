package com.example.carbuy;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AllAdsViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView brand, price;

    public AllAdsViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.adImage);
        brand = itemView.findViewById(R.id.adBrand);
        price = itemView.findViewById(R.id.adPrice);
    }

    public void showData(AllAdsItem data, Activity activity) {
        String brandModel = data.getItemBrand() + " " + data.getItemModel();
        brand.setText(brandModel);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        String formattedPrice = decimalFormat.format(Double.parseDouble(data.getItemPrice())) + "€";
        price.setText(formattedPrice);

        Glide.with(itemView.getContext())
                .load(data.getItemImageUrl())
                .into(image);
    }
}

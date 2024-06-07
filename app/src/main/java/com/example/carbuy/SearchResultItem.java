package com.example.carbuy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class SearchResultItem implements Parcelable {
    private String id, itemBrand, itemModel, itemPrice, itemImageUrl;

    public SearchResultItem(JSONObject json) {
        try {
            this.id = json.getString("car_id");
            this.itemBrand = json.getString("brand");
            this.itemModel = json.getString("model");
            this.itemPrice = json.getString("price");
            this.itemImageUrl = json.getString("image_url");
        } catch (Exception e) {
            Log.e("Error getting JSON data", e.toString());
        }
    }

    public String getId() {
        return id;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public String getItemModel() {
        return itemModel;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    // Parcelable
    protected SearchResultItem(Parcel in) {
        itemBrand = in.readString();
        itemModel = in.readString();
        itemPrice = in.readString();
        itemImageUrl = in.readString();
        id = in.readString();
    }

    public static final Parcelable.Creator<SearchResultItem> CREATOR = new Parcelable.Creator<SearchResultItem>() {
        @Override
        public SearchResultItem createFromParcel(Parcel in) {
            return new SearchResultItem(in);
        }

        @Override
        public SearchResultItem[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemBrand);
        dest.writeString(itemModel);
        dest.writeString(itemPrice);
        dest.writeString(itemImageUrl);
        dest.writeString(id);
    }
}

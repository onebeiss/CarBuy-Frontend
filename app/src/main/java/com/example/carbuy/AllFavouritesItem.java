package com.example.carbuy;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class AllFavouritesItem implements Parcelable{
    private String id, itemBrand, itemModel, itemPrice, itemImageUrl;

    public AllFavouritesItem (JSONObject json) {
        try {
            this.id = json.getString("car_id");
            this.itemBrand = json.getString("car_brand");
            this.itemModel = json.getString("car_model");
            this.itemPrice = json.getString("car_price");
            this.itemImageUrl = json.getString("image_url");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {return id;}

    public String getItemBrand() {return itemBrand;}

    public String getItemModel() {return itemModel;}

    public String getItemPrice() {return itemPrice;}

    public String getItemImageUrl() {return itemImageUrl;}

    // Parcelable
    protected AllFavouritesItem(Parcel in) {
        itemBrand = in.readString();
        itemModel = in.readString();
        itemPrice = in.readString();
        itemImageUrl = in.readString();
        id = in.readString();
    }

    public static final Parcelable.Creator<AllFavouritesItem> CREATOR = new Parcelable.Creator<AllFavouritesItem>() {
        @Override
        public AllFavouritesItem createFromParcel(Parcel in) {
            return new AllFavouritesItem(in);
        }
        @Override
        public AllFavouritesItem[] newArray(int size) {
            return new AllFavouritesItem[size];
        }
    };

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemBrand);
        dest.writeString(itemModel);
        dest.writeString(itemPrice);
        dest.writeString(itemImageUrl);
        dest.writeString(id);
    }
}

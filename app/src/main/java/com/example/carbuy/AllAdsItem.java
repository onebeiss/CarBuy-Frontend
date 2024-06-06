package com.example.carbuy;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class AllAdsItem implements Parcelable {
    private String id, itemBrand, itemModel, itemPrice, itemImageUrl;

    public AllAdsItem (JSONObject json) {
        try {
            this.id = json.getString("id");
            this.itemBrand = json.getString("brand");
            this.itemModel = json.getString("model");
            this.itemPrice = json.getString("price");
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
    protected AllAdsItem(Parcel in) {
        itemBrand = in.readString();
        itemModel = in.readString();
        itemPrice = in.readString();
        itemImageUrl = in.readString();
        id = in.readString();
    }

    public static final Creator<AllAdsItem> CREATOR = new Creator<AllAdsItem>() {
        @Override
        public AllAdsItem createFromParcel(Parcel in) {
            return new AllAdsItem(in);
        }
        @Override
        public AllAdsItem[] newArray(int size) {
            return new AllAdsItem[size];
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

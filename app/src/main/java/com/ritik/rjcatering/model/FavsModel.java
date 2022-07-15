package com.ritik.rjcatering.model;

import android.widget.ImageView;

public class FavsModel {


    private String food_ID;
    private String food_Image;
    private String food_name;
    private String food_price;

    public FavsModel(String food_ID, String food_Image, String food_name, String food_price) {
        this.food_ID = food_ID;
        this.food_Image = food_Image;
        this.food_name = food_name;
        this.food_price = food_price;
    }

    public String getFood_ID() {
        return food_ID;
    }

    public void setFood_ID(String food_ID) {
        this.food_ID = food_ID;
    }

    public String getFood_Image() {
        return food_Image;
    }

    public void setFood_Image(String food_Image) {
        this.food_Image = food_Image;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }
}

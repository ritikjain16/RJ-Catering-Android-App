package com.ritik.rjcatering.model;

public class FoodItemModel {



    private String food_ID;
    private String food_image;
    private String food_name;
    private String food_price;

    private String totalPrice;



    public FoodItemModel(String food_ID, String food_image, String food_name, String food_price) {
        this.food_ID = food_ID;
        this.food_image = food_image;
        this.food_name = food_name;
        this.food_price = food_price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFood_ID() {
        return food_ID;
    }

    public void setFood_ID(String food_ID) {
        this.food_ID = food_ID;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
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

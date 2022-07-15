package com.ritik.rjcatering.model;

public class OrdersModel {

    private String order_ID;
    private String order_Image;
    private String order_name;
    private String order_status;

    public OrdersModel(String order_ID, String order_Image, String order_name, String order_status) {
        this.order_ID = order_ID;
        this.order_Image = order_Image;
        this.order_name = order_name;
        this.order_status = order_status;
    }

    public String getOrder_ID() {
        return order_ID;
    }

    public void setOrder_ID(String order_ID) {
        this.order_ID = order_ID;
    }

    public String getOrder_Image() {
        return order_Image;
    }

    public void setOrder_Image(String order_Image) {
        this.order_Image = order_Image;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}

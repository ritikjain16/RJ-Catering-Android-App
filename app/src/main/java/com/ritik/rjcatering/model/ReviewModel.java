package com.ritik.rjcatering.model;

public class ReviewModel {
    private String review_name;
    private String review_date;
    private String review;

    public ReviewModel(String review_name, String review_date, String review) {
        this.review_name = review_name;
        this.review_date = review_date;
        this.review = review;
    }

    public String getReview_name() {
        return review_name;
    }

    public void setReview_name(String review_name) {
        this.review_name = review_name;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

package com.localdelivery.driver.model.Beans;

/**
 * Created by rishav on 28/12/16.
 */

public class RatingBeans {

    private String customerName, customerImage, customerRatings, time;

    public RatingBeans(String customerName, String customerImage, String customerRatings, String time) {
        this.customerName = customerName;
        this.customerImage = customerImage;
        this.customerRatings = customerRatings;
        this.time = time;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public String getCustomerRatings() {
        return customerRatings;
    }

    public String getTime() {
        return time;
    }
}

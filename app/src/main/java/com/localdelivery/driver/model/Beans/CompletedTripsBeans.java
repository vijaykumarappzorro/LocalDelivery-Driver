package com.localdelivery.driver.model.Beans;

/**
 * Created by rishav on 2/1/17.
 */

public class CompletedTripsBeans {

    private String orderId, price, picklocaton,droplocation,distance, customerName, tripDate, tripTime, imageUrl;

    public CompletedTripsBeans(String orderId, String price,String picklocaton,String droplocation,String disatnce ,String customerName, String tripDate, String tripTime, String imageUrl) {
        this.orderId = orderId;
        this.price = price;
        this.picklocaton =picklocaton;
        this.droplocation = droplocation;
        this.distance = disatnce;
        this.customerName = customerName;
        this.tripDate = tripDate;
        this.tripTime = tripTime;
        this.imageUrl = imageUrl;

    }

    public String getOrderId() {
        return orderId;
    }

    public String getPrice() {
        return price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public String getPicklocaton() {
        return picklocaton;
    }

    public String getDroplocation() {
        return droplocation;
    }

    public String getDistance() {
        return distance;
    }

    public String getTripTime() {
        return tripTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

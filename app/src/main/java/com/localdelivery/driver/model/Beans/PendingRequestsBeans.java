package com.localdelivery.driver.model.Beans;

/**
 * Created by rishav on 4/1/17.
 */

public class PendingRequestsBeans {

    private String request_id, customer_id, first_name, last_name, pickup_location, drop_location, price,
                    source_lat, source_lng, destination_lat, destination_lng;

    public PendingRequestsBeans(String request_id, String customer_id, String first_name, String last_name,
                                String pickup_location, String drop_location, String price,
                                String source_lat, String source_lng, String destination_lat, String destination_lng) {
        this.request_id = request_id;
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.pickup_location = pickup_location;
        this.drop_location = drop_location;
        this.price = price;
        this.source_lat = source_lat;
        this.source_lng = source_lng;
        this.destination_lat = destination_lat;
        this.destination_lng = destination_lng;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public String getDrop_location() {
        return drop_location;
    }

    public String getPrice() {
        return price;
    }

    public String getSource_lat() {
        return source_lat;
    }

    public String getSource_lng() {
        return source_lng;
    }

    public String getDestination_lat() {
        return destination_lat;
    }

    public String getDestination_lng() {
        return destination_lng;
    }
}

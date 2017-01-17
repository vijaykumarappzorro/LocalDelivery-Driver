package com.localdelivery.driver.model;


public class Event {
    private int key;
    private String value;
    private String id, imageUrl,fisrtname,lastname,emailid;
    private String request_id, customer_id, pickup_location, drop_location, price;

    public Event(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public Event(int key, String id, String imageUrl, String fisrtname, String lastname, String emailid) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.key = key;
        this.fisrtname =fisrtname;
        this.lastname = lastname;
        this.emailid = emailid;
    }

    public Event(int key, String fisrtname, String lastname, String request_id, String customer_id, String pickup_location, String drop_location, String price) {
        this.key = key;
        this.fisrtname = fisrtname;
        this.lastname = lastname;
        this.request_id = request_id;
        this.customer_id = customer_id;
        this.pickup_location = pickup_location;
        this.drop_location = drop_location;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFisrtname() {
        return fisrtname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getCustomer_id() {
        return customer_id;
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
}

package com.localdelivery.driver.model;


public class Event {
    private int key;
    private String value;
    private String id, name, imageUrl,fisrtname,lastname,emailid;

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


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
}

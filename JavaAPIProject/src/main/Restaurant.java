package com.example;

public class Restaurant {
    private String name;
    private double latitude;
    private double longitude;
    private double rating;

    public Restaurant(String name, double lat, double lng, double rating) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lng;
        this.rating = rating;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getRating(){
        return rating;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Rating: %.1f)", name, rating);
    }
}
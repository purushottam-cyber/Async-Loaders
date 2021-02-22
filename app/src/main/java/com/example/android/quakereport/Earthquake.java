package com.example.android.quakereport;

public class Earthquake {
    private Double magnitude;
    private String place;
    private long timeInMillisecond;
    private String url;


    public Earthquake(Double magnitude, String place, long timeInMillisecond,String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.timeInMillisecond = timeInMillisecond;
        this.url = url;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public long getTimeInMillisecond() {
        return timeInMillisecond;
    }

    public void setTimeInMillisecond(long timeInMillisecond) {
        this.timeInMillisecond = timeInMillisecond;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package ru.geekbrains.weatherapplication.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Coordinates  {

    @SerializedName("lat")
    @Expose
    private float lat;

    @SerializedName("lon")
    @Expose
    private float lon;


    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

}

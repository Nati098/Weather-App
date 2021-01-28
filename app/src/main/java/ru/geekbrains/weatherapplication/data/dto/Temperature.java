package ru.geekbrains.weatherapplication.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Temperature {

    @SerializedName("day")
    @Expose
    private double day;

    @SerializedName("min")
    @Expose
    private double min;

    @SerializedName("max")
    @Expose
    private double max;


    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

}

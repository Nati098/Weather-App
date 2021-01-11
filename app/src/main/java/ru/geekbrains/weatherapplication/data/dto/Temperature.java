package ru.geekbrains.weatherapplication.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Temperature implements Parcelable {

    private double day;
    private double min;
    private double max;

    protected Temperature(Parcel in) {
        day = in.readDouble();
        min = in.readDouble();
        max = in.readDouble();
    }

    public static final Creator<Temperature> CREATOR = new Creator<Temperature>() {
        @Override
        public Temperature createFromParcel(Parcel in) {
            return new Temperature(in);
        }

        @Override
        public Temperature[] newArray(int size) {
            return new Temperature[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(day);
        parcel.writeDouble(min);
        parcel.writeDouble(max);
    }
}

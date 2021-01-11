package ru.geekbrains.weatherapplication.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Wind implements Parcelable {
    private int speed;
    private int deg;

    protected Wind(Parcel in) {
        speed = in.readInt();
        deg = in.readInt();
    }

    public static final Creator<Wind> CREATOR = new Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(speed);
        parcel.writeInt(deg);
    }
}


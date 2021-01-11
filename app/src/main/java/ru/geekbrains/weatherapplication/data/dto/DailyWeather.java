package ru.geekbrains.weatherapplication.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyWeather implements Parcelable {

    private Temperature temp;
    private Weather[] weather;

    protected DailyWeather(Parcel in) {
        temp = in.readParcelable(Temperature.class.getClassLoader());
        weather = in.createTypedArray(Weather.CREATOR);
    }

    public static final Creator<DailyWeather> CREATOR = new Creator<DailyWeather>() {
        @Override
        public DailyWeather createFromParcel(Parcel in) {
            return new DailyWeather(in);
        }

        @Override
        public DailyWeather[] newArray(int size) {
            return new DailyWeather[size];
        }
    };

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(temp, i);
        parcel.writeTypedArray(weather, i);
    }
}

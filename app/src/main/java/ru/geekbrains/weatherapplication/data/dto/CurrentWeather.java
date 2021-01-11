package ru.geekbrains.weatherapplication.data.dto;

import android.os.Parcel;
import android.os.Parcelable;


public class CurrentWeather implements Parcelable {

    private int sunrise;
    private int sunset;
    private double temp;
    private double feelsLike;
    private int pressure;
    private int humidity;
    private Weather[] weather;

    public CurrentWeather() {}

    public CurrentWeather(Parcel in) {
        sunrise = in.readInt();
        sunset = in.readInt();
        temp = in.readDouble();
        feelsLike = in.readDouble();
        pressure = in.readInt();
        humidity = in.readInt();
        weather = in.createTypedArray(Weather.CREATOR);
    }

    public static final Creator<CurrentWeather> CREATOR = new Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel in) {
            return new CurrentWeather(in);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
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
        parcel.writeInt(sunrise);
        parcel.writeInt(sunset);
        parcel.writeDouble(temp);
        parcel.writeDouble(feelsLike);
        parcel.writeInt(pressure);
        parcel.writeInt(humidity);
        parcel.writeTypedArray(weather, 0);
    }
}

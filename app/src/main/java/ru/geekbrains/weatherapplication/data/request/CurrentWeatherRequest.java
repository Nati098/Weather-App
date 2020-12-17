package ru.geekbrains.weatherapplication.data.request;

import android.os.Parcel;

import ru.geekbrains.weatherapplication.data.dto.Clouds;
import ru.geekbrains.weatherapplication.data.dto.Coordinates;
import ru.geekbrains.weatherapplication.data.dto.Weather;
import ru.geekbrains.weatherapplication.data.dto.Main;
import ru.geekbrains.weatherapplication.data.dto.Wind;


public class CurrentWeatherRequest implements MainRequest {
    private Coordinates coord;
    private Weather[] weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private String name;

    public CurrentWeatherRequest() {}

    public CurrentWeatherRequest(Parcel in) {
        coord = in.readParcelable(Coordinates.class.getClassLoader());
        weather = in.createTypedArray(Weather.CREATOR);
        main = in.readParcelable(Main.class.getClassLoader());
        wind = in.readParcelable(Wind.class.getClassLoader());
        clouds = in.readParcelable(Clouds.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<CurrentWeatherRequest> CREATOR = new Creator<CurrentWeatherRequest>() {
        @Override
        public CurrentWeatherRequest createFromParcel(Parcel in) {
            return new CurrentWeatherRequest(in);
        }

        @Override
        public CurrentWeatherRequest[] newArray(int size) {
            return new CurrentWeatherRequest[size];
        }
    };

    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(coord,0);
        parcel.writeTypedArray(weather, 0);
        parcel.writeParcelable(main,0);
        parcel.writeParcelable(wind,0);
        parcel.writeParcelable(clouds,0);
        parcel.writeString(name);
    }
}

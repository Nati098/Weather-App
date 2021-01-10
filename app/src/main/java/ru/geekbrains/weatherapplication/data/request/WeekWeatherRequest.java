package ru.geekbrains.weatherapplication.data.request;

import android.os.Parcel;

import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.DailyWeather;
import ru.geekbrains.weatherapplication.data.dto.Main;
import ru.geekbrains.weatherapplication.data.dto.Weather;


public class WeekWeatherRequest implements WeatherRequest {

    private CurrentWeather current;
    private DailyWeather[] daily;

    public WeekWeatherRequest() {}

    public WeekWeatherRequest(Parcel in) {
        current = in.readParcelable(CurrentWeather.class.getClassLoader());
        daily = in.createTypedArray(DailyWeather.CREATOR);
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

    @Override
    public Weather getFirstWeather() {
        return current.getWeather()[0];
    }

    @Override
    public Main getMain() {
        return null;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public DailyWeather[] getDaily() {
        return daily;
    }

    public void setDaily(DailyWeather[] daily) {
        this.daily = daily;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(current,0);
        parcel.writeTypedArray(daily,0);
    }
}

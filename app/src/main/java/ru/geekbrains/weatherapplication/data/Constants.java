package ru.geekbrains.weatherapplication.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.item.OptionItem;


public class Constants {
    public static final float ABSOLUTE_ZERO = 273.15f;

    public static final String CITY_LIST_FILE_PATH = "city.list.json";

    public static final String GET_WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String GET_WEEK_WEATHER_URL = "https://api.openweathermap.org/data/2.5/";

    public static final String WEATHER_OPTIONS = "weather_options";

    public static final String SUNRISE_TIME_OPTION = "sunrise_time_option";
    public static final String SUNSET_TIME_OPTION = "sunset_time_option";
    public static final String TEMPERATURE_OPTION = "temperature_options";
    public static final String FEELSLIKE_OPTION = "feelslike_options";
    public static final String ATM_PRESSURE_OPTION = "atm_pressure_options";
    public static final String WIND_OPTION = "wind_options";
    public static final String HUMIDITY_OPTION = "humidity_options";

    public static class LoggerMode {
        public static boolean VERBOSE = true;
        public static boolean DEBUG = VERBOSE && true;
    }

    public static int getWeatherImage(String icon) {
        switch (icon) {
            case "01d":
                return R.drawable.ic_clear_sun;
            case "01n":
                return R.drawable.ic_clear_moon;
            case "02d":
                return R.drawable.ic_cloudy_sun;
            case "02n":
                return R.drawable.ic_cloudy_moon;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                return R.drawable.ic_cloudy;
            case "09d":
            case "09n":
                return R.drawable.ic_rainy;
            case "10d":
                return R.drawable.ic_rainy_sun;
            case "10n":
                return R.drawable.ic_rainy_moon;
            case "11d":
            case "11n":
                return R.drawable.ic_zip;
            case "13d":
            case "13n":
                return R.drawable.ic_snowy;
            case "50d":
            case "50n":
                return R.drawable.ic_mist;
            default:
                return R.drawable.ic_empty;
        }
    }

    public static String getFormattedExtraInfo(OptionItem item, CurrentWeather newData) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();

        switch (item.getId()) {
            case FEELSLIKE_OPTION:
                return String.format(sb.append("%.0f ").append("°C").toString(), newData.getFeelsLike()-ABSOLUTE_ZERO);
            case SUNRISE_TIME_OPTION:
                calendar.setTimeInMillis(newData.getSunrise());
                return formatter.format(calendar.getTime());
            case SUNSET_TIME_OPTION:
                calendar.setTimeInMillis(newData.getSunset());
                return formatter.format(calendar.getTime());
            case ATM_PRESSURE_OPTION:
                return String.format(sb.append("%d ").append("hPa").toString(), newData.getPressure());
            case HUMIDITY_OPTION:
                return String.format(sb.append("%d ").append("%%").toString(), newData.getHumidity());
            case WIND_OPTION:
            default:
                return "";
        }
    }

}

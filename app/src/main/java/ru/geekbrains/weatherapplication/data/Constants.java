package ru.geekbrains.weatherapplication.data;

import java.util.Arrays;
import java.util.List;

import ru.geekbrains.weatherapplication.item.OptionItem;

import static ru.geekbrains.weatherapplication.data.SystemPreferences.IS_NIGHT_MODE;

public class Constants {
    public static final String WEATHER_OPTIONS = "weather_options";

    public static final List<OptionItem> settings = Arrays.asList(new OptionItem(IS_NIGHT_MODE, "Night Mode", false));

    public static class LoggerMode {
        public static boolean VERBOSE = true;
        public static boolean DEBUG = VERBOSE && true;
    }

}

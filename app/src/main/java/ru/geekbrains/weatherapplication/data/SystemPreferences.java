package ru.geekbrains.weatherapplication.data;

import android.content.Context;
import android.content.SharedPreferences;

import ru.geekbrains.weatherapplication.BaseAppActivity;


public class SystemPreferences {
    private static String SYSTEM_PREFERENCES = "system_preferences";

    public static final String LAST_REQUESTED_CITY = "is_night_mode";
    public static final String IS_NIGHT_MODE = "is_night_mode";


    private static SharedPreferences getSP() {
        return BaseAppActivity.getContext().getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static boolean getBooleanPreference(String key) {
        return getSP().getBoolean(key, false);
    }

    public static String getStringPreference(String key) {
        return getSP().getString(key, "");
    }

    public static void setPreference(String key, String value) {
        getSP().edit().putString(key, value).apply();
    }

    public static void setPreference(String key, boolean value) {
        getSP().edit().putBoolean(key, value).apply();
    }
}

package ru.geekbrains.weatherapplication;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.geekbrains.weatherapplication.room.CityDao;
import ru.geekbrains.weatherapplication.room.CityDatabase;

public class BaseApp extends Application {

    private static BaseApp instance;
    private CityDatabase db;

    public static BaseApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(getApplicationContext(), CityDatabase.class, "city_database")
                .allowMainThreadQueries()
                .build();
    }

    public CityDao getCityDao() {
        return db.getCityDao();
    }
}

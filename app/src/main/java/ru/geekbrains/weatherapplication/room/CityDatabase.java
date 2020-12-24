package ru.geekbrains.weatherapplication.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {CityEntity.class}, version = 1)
public abstract class CityDatabase extends RoomDatabase {
    public abstract CityDao getCityDao();
}

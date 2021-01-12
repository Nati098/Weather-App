package ru.geekbrains.weatherapplication.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(CityEntity city);

    @Insert
    void insertAll(CityEntity... users);

    @Update
    void updateCity(CityEntity city);

    @Delete
    void deleteCity(CityEntity city);

    @Query("SELECT COUNT() FROM CityEntity")
    long getCountCities();

    @Query("SELECT * FROM CityEntity")
    List<CityEntity> getAllCities();

    @Query("SELECT * FROM CityEntity WHERE id = :id")
    CityEntity getCityById(int id);

    @Query("SELECT * FROM CityEntity WHERE name = :name")
    CityEntity getCityByName(String name);

    @Query("UPDATE CityEntity SET tempr = :t WHERE id = :id")
    int updateTempr(int id, double t);

    @Query("DELETE FROM CityEntity WHERE id = :id")
    void deleteCityById(int id);

}

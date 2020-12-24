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

    @Update
    void updateCity(CityEntity city);

    @Delete
    void deleteCity(CityEntity city);

    @Query("SELECT COUNT() FROM city")
    long getCountCities();

    @Query("SELECT * FROM city")
    List<CityEntity> getAllCities();

    @Query("SELECT * FROM city WHERE id = :id")
    CityEntity getStudentById(long id);

    @Query("DELETE FROM city WHERE id = :id")
    void deleteCityById(long id);

}

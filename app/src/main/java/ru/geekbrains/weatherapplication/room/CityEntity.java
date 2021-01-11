package ru.geekbrains.weatherapplication.room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name",  "state",  "country", "lon", "lat", "temp"})})
public class CityEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "lon")
    public int lon;

    @ColumnInfo(name = "lat")
    public int lat;

    @ColumnInfo(name = "temp")
    public int temp;

}

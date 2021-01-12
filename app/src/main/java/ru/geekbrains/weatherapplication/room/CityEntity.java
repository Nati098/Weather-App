package ru.geekbrains.weatherapplication.room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name",  "state",  "country", "lon", "lat", "tempr"})})
public class CityEntity {

    @PrimaryKey()
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "lon")
    public float lon;

    @ColumnInfo(name = "lat")
    public float lat;

    @ColumnInfo(name = "tempr")
    public double tempr;

}

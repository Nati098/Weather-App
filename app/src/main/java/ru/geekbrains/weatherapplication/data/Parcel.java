package ru.geekbrains.weatherapplication.data;

import java.io.Serializable;
import java.util.List;

import ru.geekbrains.weatherapplication.item.OptionItem;

public class Parcel implements Serializable {
    public String cityName;
    public List<OptionItem> options;
}
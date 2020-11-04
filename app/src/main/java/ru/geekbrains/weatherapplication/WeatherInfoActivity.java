package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.adapter.CurrentWeatherExtraAdapter;
import ru.geekbrains.weatherapplication.adapter.WeatherWeekAdapter;
import ru.geekbrains.weatherapplication.item.CurrentWeatherExtraItem;
import ru.geekbrains.weatherapplication.item.WeatherItem;

public class WeatherInfoActivity extends AppCompatActivity {

    private CurrentWeatherExtraAdapter extraInfoAdapter;
    private RecyclerView extraInfoRecycler;

    private WeatherWeekAdapter weatherDayAdapter;
    private RecyclerView weatherDayRecycler;

    private WeatherWeekAdapter weatherWeekAdapter;
    private RecyclerView weatherWeekRecycler;

    private ImageButton btnSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        bindView();
        setupRecycler();
    }

    private void bindView() {

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener((view -> startActivity(new Intent(this, SettingsActivity.class))));

        weatherDayRecycler = findViewById(R.id.weather_day_recycler);
        weatherWeekRecycler = findViewById(R.id.weather_week_recycler);

        extraInfoRecycler = findViewById(R.id.extra_info_recycler);
        weatherWeekRecycler = findViewById(R.id.weather_week_recycler);

    }

    private void setupRecycler() {
        extraInfoAdapter = new CurrentWeatherExtraAdapter(this, generateExtraInfoList(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        extraInfoRecycler.setAdapter(extraInfoAdapter);
        extraInfoRecycler.setLayoutManager(new LinearLayoutManager(this));


        weatherDayAdapter = new WeatherWeekAdapter(this, R.layout.weather_day_item_list,
                generateWeatherDayList(), new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
        weatherDayRecycler.setAdapter(weatherDayAdapter);
        weatherDayRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        weatherWeekAdapter = new WeatherWeekAdapter(this, R.layout.weather_week_item_list,
                generateWeatherWeekList(), new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
        weatherWeekRecycler.setAdapter(weatherWeekAdapter);
        weatherWeekRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<CurrentWeatherExtraItem> generateExtraInfoList() {
        List<CurrentWeatherExtraItem> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new CurrentWeatherExtraItem(R.drawable.ic_temp_normal,
                    String.format(getString(R.string.extra_item_label), i+1),
                    String.format("7%s", getString(R.string.temperature_unit))));
        }
        return data;
    }

    public List<WeatherItem> generateWeatherDayList() {
        List<WeatherItem> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            data.add(new WeatherItem(getString(R.string.time_dummy), R.drawable.ic_rainy_moon, i+7));
        }
        return data;
    }

    public List<WeatherItem> generateWeatherWeekList() {
        List<WeatherItem> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            data.add(new WeatherItem(getString(R.string.sunday), R.drawable.ic_cloudy, i+7));
        }
        return data;
    }
}
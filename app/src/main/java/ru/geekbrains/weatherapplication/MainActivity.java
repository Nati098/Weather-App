package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.adapter.CurrentWeatherExtraAdapter;
import ru.geekbrains.weatherapplication.adapter.WeatherWeekAdapter;
import ru.geekbrains.weatherapplication.data.CurrentWeatherExtra;
import ru.geekbrains.weatherapplication.data.WeatherItem;

public class MainActivity extends AppCompatActivity {

    private CurrentWeatherExtraAdapter extraInfoAdapter;
    private RecyclerView extraInfoRecycler;

    private WeatherWeekAdapter weatherDayAdapter;
    private RecyclerView weatherDayRecycler;

    private WeatherWeekAdapter weatherWeekAdapter;
    private RecyclerView weatherWeekRecycler;

    private Toolbar toolbar;
    private ImageView currentWeather;
    private TextView currentTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        setupRecycler();
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        currentWeather = findViewById(R.id.current_weather);
        currentTemp = findViewById(R.id.current_temp);

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
        weatherWeekRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public List<CurrentWeatherExtra> generateExtraInfoList() {
        List<CurrentWeatherExtra> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new CurrentWeatherExtra(R.drawable.ic_temp_normal,
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
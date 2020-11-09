package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.adapter.CurrentWeatherExtraAdapter;
import ru.geekbrains.weatherapplication.adapter.WeatherWeekAdapter;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.item.CurrentWeatherExtraItem;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.item.WeatherItem;

import static ru.geekbrains.weatherapplication.data.Constants.WEATHER_OPTIONS;


public class WeatherInfoActivity extends AppCompatActivity {
    private static final String ADDRESS_WEATHER = "https://www.gismeteo.ru/";

    private TextView toolbarTitle;

    private CurrentWeatherExtraAdapter extraInfoAdapter;
    private RecyclerView extraInfoRecycler;

    private WeatherWeekAdapter weatherDayAdapter;
    private RecyclerView weatherDayRecycler;

    private WeatherWeekAdapter weatherWeekAdapter;
    private RecyclerView weatherWeekRecycler;

    private ImageButton btnSettings;
    private Button btnMoreInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        Parcel parcel = (Parcel) getIntent().getExtras().getSerializable(WEATHER_OPTIONS);

        bindView();
        setupRecycler(parcel.options);

        String title = getString(R.string.weather_info_title, parcel.cityName);
        Log.d("WeatherInfoActivity", "title = "+title);
        toolbarTitle.setText(title);
    }

    private void bindView() {
        toolbarTitle = findViewById(R.id.toolbar_title);

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener((view -> startActivity(new Intent(this, SettingsActivity.class))));

        weatherDayRecycler = findViewById(R.id.weather_day_recycler);
        weatherWeekRecycler = findViewById(R.id.weather_week_recycler);

        extraInfoRecycler = findViewById(R.id.extra_info_recycler);
        weatherWeekRecycler = findViewById(R.id.weather_week_recycler);

        btnMoreInfo = findViewById(R.id.btn_more_info);
        btnMoreInfo.setOnClickListener(view -> {
            Uri uri = Uri.parse(ADDRESS_WEATHER);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }

    private void setupRecycler(List<OptionItem> options) {
        extraInfoAdapter = new CurrentWeatherExtraAdapter(this, generateExtraInfoList(options), (adapterView, view, i, l) -> { });
        extraInfoRecycler.setAdapter(extraInfoAdapter);
        extraInfoRecycler.setLayoutManager(new LinearLayoutManager(this));


        weatherDayAdapter = new WeatherWeekAdapter(this, R.layout.weather_day_item_list,
                generateWeatherDayList(), (adapterView, view, i, l) -> { });
        weatherDayRecycler.setAdapter(weatherDayAdapter);
        weatherDayRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        weatherWeekAdapter = new WeatherWeekAdapter(this, R.layout.weather_week_item_list,
                generateWeatherWeekList(), (adapterView, view, i, l) -> { });
        weatherWeekRecycler.setAdapter(weatherWeekAdapter);
        weatherWeekRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<CurrentWeatherExtraItem> generateExtraInfoList(List<OptionItem> options) {
        List<CurrentWeatherExtraItem> data = new ArrayList<>();

        options.forEach(optionItem -> {
            if (optionItem.isActive()) {
                data.add(new CurrentWeatherExtraItem(R.drawable.ic_temp_normal,
                        optionItem.getLabel(),
                        String.format("%d%s", 7,getString(R.string.temperature_unit))));
            }
        });

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
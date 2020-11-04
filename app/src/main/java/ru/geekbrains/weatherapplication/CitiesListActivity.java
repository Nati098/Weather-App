package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.adapter.OptionsAdapter;
import ru.geekbrains.weatherapplication.item.OptionItem;


public class CitiesListActivity extends AppCompatActivity {

    private Button btnSeeWeather;
    private ImageButton btnSettings;

    private OptionsAdapter optionsAdapter;
    private RecyclerView optionsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        bindView();
        setupRecycler();
    }

    private void bindView() {
        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener((view -> startActivity(new Intent(this, SettingsActivity.class))));

        btnSeeWeather = findViewById(R.id.btn_see_weather);
        btnSeeWeather.setOnClickListener((v) -> {
            Intent intent = new Intent(this, WeatherInfoActivity.class);
            startActivity(intent);
        });

        optionsRecycler = findViewById(R.id.recycler);

    }

    private void setupRecycler() {
        optionsAdapter = new OptionsAdapter(this, generateOptionsList(), (adapterView, view, i, l) -> { });
        optionsRecycler.setAdapter(optionsAdapter);
        optionsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<OptionItem> generateOptionsList() {
        List<OptionItem> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new OptionItem(String.format(getString(R.string.extra_item_label), i+1), i%2==0));
            Log.v("CitiesListActivity", "item #"+ i + " - " + (i%2==0));
        }
        return data;
    }


}
package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import ru.geekbrains.weatherapplication.adapter.OptionsAdapter;

import static ru.geekbrains.weatherapplication.data.SettingsList.settings;


public class SettingsActivity extends AppCompatActivity {

    private OptionsAdapter optionsAdapter;
    private RecyclerView optionsRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindView();
        setupRecycler();
    }

    private void bindView() {
        optionsRecycler = findViewById(R.id.recycler);
    }

    private void setupRecycler() {
        optionsAdapter = new OptionsAdapter(this, settings, (adapterView, view, i, l) -> { });
        optionsRecycler.setAdapter(optionsAdapter);
        optionsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
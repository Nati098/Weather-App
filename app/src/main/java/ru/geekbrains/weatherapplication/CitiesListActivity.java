package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.adapter.OptionsAdapter;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.item.OptionItem;

import static ru.geekbrains.weatherapplication.data.Constants.WEATHER_OPTIONS;


public class CitiesListActivity extends AppCompatActivity {

    private static Context context;

    private EditText editTextCityName;
    private Button btnSeeWeather;
    private ImageButton btnSettings;

    private OptionsAdapter optionsAdapter;
    private RecyclerView optionsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        context = getApplicationContext();

        bindView();
        setupRecycler();
    }

    private void bindView() {
        editTextCityName = findViewById(R.id.city_name_edittext);
        editTextCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() != 0) {
                    btnSeeWeather.setEnabled(true);
                }

                else {
                    btnSeeWeather.setEnabled(false);
                }
            }
        });

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener((view -> startActivity(new Intent(this, SettingsActivity.class))));

        Log.d("CitiesListActivity","editTextCityName isEmpty():"+ !editTextCityName.getText().toString().isEmpty());
        btnSeeWeather = findViewById(R.id.btn_see_weather);
        btnSeeWeather.setEnabled(!editTextCityName.getText().toString().isEmpty());
        btnSeeWeather.setOnClickListener((v) -> {
            Parcel parcel = new Parcel();
            parcel.cityName = editTextCityName.getText().toString();
            parcel.options = optionsAdapter.getData();

            Intent intent = new Intent(this, WeatherInfoActivity.class);
            intent.putExtra(WEATHER_OPTIONS, parcel);
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
            data.add(new OptionItem("item_"+ i, String.format(getString(R.string.extra_item_label), i+1), i%2==0));
            Log.v("CitiesListActivity", "item #"+ i + " - " + (i%2==0));
        }
        return data;
    }

    public static Context getContext() {
        return context;
    }
}
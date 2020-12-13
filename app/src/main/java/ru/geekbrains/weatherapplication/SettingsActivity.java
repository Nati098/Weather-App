package ru.geekbrains.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;

import ru.geekbrains.weatherapplication.data.SystemPreferences;


public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switchNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindView();
    }

    private void bindView() {
        switchNightMode = findViewById(R.id.switch_night_mode);
        switchNightMode.setChecked(SystemPreferences.getBooleanPreference(SystemPreferences.IS_NIGHT_MODE));
        switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SystemPreferences.setPreference(SystemPreferences.IS_NIGHT_MODE, isChecked);
            this.recreate();
        });
    }

}
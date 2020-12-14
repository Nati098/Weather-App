package ru.geekbrains.weatherapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import java.util.Arrays;
import java.util.List;

import ru.geekbrains.weatherapplication.data.SystemPreferences;
import ru.geekbrains.weatherapplication.fragment.CitiesListFragment;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.utils.OpenFragmentListener;

import static ru.geekbrains.weatherapplication.data.Constants.*;
import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;


public class BaseAppActivity extends AppCompatActivity implements OpenFragmentListener {
    private static final int SETTINGS_CODE = 88;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_app);

        context = getApplicationContext();

        if (SystemPreferences.getBooleanPreference(SystemPreferences.IS_NIGHT_MODE)) {
            setTheme(R.style.AppCustomDarkTheme);
        }
        else {
            setTheme(R.style.AppCustomLightTheme);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openFragment(CitiesListFragment.newInstance("", getWeatherExtraInfo()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE) {
            recreate();
        }
    }

    public static Context getContext() {
        return context;
    }

    public List<OptionItem> getWeatherExtraInfo() {
        return Arrays.asList(//new OptionItem(SUNRISE_TIME_OPTION, Resources.getSystem().getString(R.string.sunrise_extra_option), SystemPreferences.getBooleanPreference(SUNRISE_TIME_OPTION)),
                //new OptionItem(SUNSET_TIME_OPTION, Resources.getSystem().getString(R.string.sunset_extra_option), SystemPreferences.getBooleanPreference(SUNSET_TIME_OPTION)),
                //new OptionItem(TEMPERATURE_OPTION, Resources.getSystem().getString(R.string.temp_extra_option), SystemPreferences.getBooleanPreference(TEMPERATURE_OPTION)),
                //new OptionItem(ATM_PRESSURE_OPTION, Resources.getSystem().getString(R.string.atm_pressure_extra_option), SystemPreferences.getBooleanPreference(ATM_PRESSURE_OPTION)),
                new OptionItem(WIND_OPTION, "Wind", SystemPreferences.getBooleanPreference(WIND_OPTION)),
                new OptionItem(HUMIDITY_OPTION, getString(R.string.humidity_option), SystemPreferences.getBooleanPreference(HUMIDITY_OPTION)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings) {
            startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SETTINGS_CODE);
        }
        return true;
    }

    @Override
    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    @Override
    public void openFragment(int id,Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(id, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }

        if (DEBUG) {
            Log.d("BaseAppActivity", "onBackPressed -> remained in stack: "+getSupportFragmentManager().getBackStackEntryCount());
        }
    }
}
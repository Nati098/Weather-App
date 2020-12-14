package ru.geekbrains.weatherapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

import ru.geekbrains.weatherapplication.data.SystemPreferences;
import ru.geekbrains.weatherapplication.fragment.CitiesListFragment;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.utils.OpenFragmentListener;

import static ru.geekbrains.weatherapplication.data.Constants.*;
import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;


public class BaseAppActivity extends AppCompatActivity implements OpenFragmentListener, NavigationView.OnNavigationItemSelectedListener {
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

        bindView();

        addFragment(CitiesListFragment.newInstance("", getWeatherExtraInfo()));
    }

    private void bindView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_baseapp);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_baseapp);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings) {
            startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SETTINGS_CODE);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_forecast) {
            Toast.makeText(getApplicationContext(), "NAV FORECAST", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(), "NAV ABOUT", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_baseapp);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void addFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(id, fragment)
                .commit();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_baseapp);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

}
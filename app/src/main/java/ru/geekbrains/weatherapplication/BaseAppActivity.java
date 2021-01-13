package ru.geekbrains.weatherapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;

import ru.geekbrains.weatherapplication.data.SystemPreferences;
import ru.geekbrains.weatherapplication.fragment.CitiesListFragment;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.service.ConnectivityNotificationReceiver;
import ru.geekbrains.weatherapplication.service.ServiceNotificationReceiver;
import ru.geekbrains.weatherapplication.utils.OpenFragmentListener;

import static ru.geekbrains.weatherapplication.data.Constants.*;
import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;


public class BaseAppActivity extends AppCompatActivity implements OpenFragmentListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = BaseAppActivity.class.getSimpleName();
    private static final int SETTINGS_CODE = 88;

    private static Context context;

    private ServiceNotificationReceiver lowBatteryReceiver;
    private ConnectivityNotificationReceiver noConnectionReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        if (SystemPreferences.getBooleanPreference(SystemPreferences.IS_NIGHT_MODE)) {
            setTheme(R.style.AppCustomDarkTheme);
        }
        else {
            setTheme(R.style.AppCustomLightTheme);
        }
        setContentView(R.layout.activity_base_app);

        bindView();
        initGetToken();
        initNotificationChannel();

        initBroadcastReceivers();

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

    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "PushMessage -> getInstanceId failed", task.getException());
                        return;
                    }

                    String token = task.getResult().getToken();
                    if (DEBUG) {
                        Log.d(TAG, "PushMessage -> got token="+token);
                    }
                });
    }

    private void initBroadcastReceivers() {
        lowBatteryReceiver = new ServiceNotificationReceiver();
        IntentFilter lowBatteryInf = new IntentFilter();
        lowBatteryInf.addAction("android.intent.action.BATTERY_LOW");
        registerReceiver(lowBatteryReceiver, lowBatteryInf);

        noConnectionReceiver = new ConnectivityNotificationReceiver();
        registerReceiver(lowBatteryReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
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
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                addFragment(CitiesListFragment.newInstance("", getWeatherExtraInfo()));
            }
            else {
                startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SETTINGS_CODE);
            }

        } else if (id == R.id.nav_about) {
            //startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            createAlertDialog();
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(lowBatteryReceiver);
        unregisterReceiver(noConnectionReceiver);
        super.onDestroy();
    }

    public static Context getContext() {
        return context;
    }

    public List<OptionItem> getWeatherExtraInfo() {
        return Arrays.asList(new OptionItem(FEELSLIKE_OPTION, getString(R.string.feelslike_extra_option), SystemPreferences.getBooleanPreference(FEELSLIKE_OPTION)),
                new OptionItem(SUNRISE_TIME_OPTION, getString(R.string.sunrise_extra_option), SystemPreferences.getBooleanPreference(SUNRISE_TIME_OPTION)),
                new OptionItem(SUNSET_TIME_OPTION, getString(R.string.sunset_extra_option), SystemPreferences.getBooleanPreference(SUNSET_TIME_OPTION)),
                new OptionItem(ATM_PRESSURE_OPTION, getString(R.string.atm_pressure_extra_option), SystemPreferences.getBooleanPreference(ATM_PRESSURE_OPTION)),
                //new OptionItem(WIND_OPTION, getString(R.string.wind_option), SystemPreferences.getBooleanPreference(WIND_OPTION)),
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

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_item_about)
                .setMessage(R.string.about_description)
                .setIcon(R.drawable.ic_about)
                .setCancelable(true)
                .setPositiveButton(R.string.button_ok, (dialog, id) -> {});

        AlertDialog alert = builder.create();
        alert.show();
    }

}
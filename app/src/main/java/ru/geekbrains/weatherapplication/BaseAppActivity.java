package ru.geekbrains.weatherapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.data.SystemPreferences;
import ru.geekbrains.weatherapplication.fragment.CitiesListFragment;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.utils.OpenFragmentListener;

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

        ImageButton btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener((v -> startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SETTINGS_CODE)));

        openFragment(CitiesListFragment.newInstance("", generateOptionsList()));
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

    public List<OptionItem> generateOptionsList() {
        List<OptionItem> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new OptionItem("item_"+ i, String.format(getString(R.string.extra_item_label), i+1), i%2==0));
            if (DEBUG) {
                Log.d("BaseAppActivity", "item #" + i + " - " + (i % 2 == 0));
            }
        }
        return data;
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
package ru.geekbrains.weatherapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.adapter.OptionsAdapter;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.utils.OpenFragmentListener;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;
import static ru.geekbrains.weatherapplication.data.Constants.WEATHER_OPTIONS;


public class CitiesListFragment extends Fragment {

    private TextInputEditText editTextCityName;
    private Button btnSeeWeather;

    private OptionsAdapter optionsAdapter;
    private RecyclerView optionsRecycler;

    private OpenFragmentListener openFragmentListener;
//    private boolean isExtraInfo;  // if we can draw 2nd fragment on that screen


    public static CitiesListFragment newInstance(String cityName, List<OptionItem> data) {
        CitiesListFragment fragment = new CitiesListFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER_OPTIONS, new Parcel(cityName, data));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OpenFragmentListener) {
            openFragmentListener = (OpenFragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);

        Parcel parcel = (Parcel) getArguments().getSerializable(WEATHER_OPTIONS);
        if (savedInstanceState != null) {
            if (DEBUG) {
                Log.d("CitiesListFragment", "onViewCreated: savedInstanceState != null");
            }
            parcel = (Parcel) savedInstanceState.getSerializable(WEATHER_OPTIONS);
        }

        editTextCityName.setText(parcel.cityName);
        setupRecycler(view, parcel.options);

//        isExtraInfo = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
//        if (isExtraInfo) {
//            showExtraInfoFragment();
//        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Parcel parcel = new Parcel(editTextCityName.getText().toString(), optionsAdapter.getData());
        outState.putSerializable(WEATHER_OPTIONS, parcel);
        if (DEBUG) {
            Log.d("CitiesListFragment", "onSaveInstanceState");
        }
        super.onSaveInstanceState(outState);
    }


    private void bindView(View view) {
        editTextCityName = view.findViewById(R.id.city_name_edittext);
        editTextCityName.setFocusable(true);
        editTextCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validate((TextView) editTextCityName, getResources().getString(R.string.empty_city_name_error));
            }
        });

        if (DEBUG) {
            Log.d("CitiesListFragment", "editTextCityName isEmpty():" + !editTextCityName.getText().toString().isEmpty());
        }
        btnSeeWeather = view.findViewById(R.id.btn_see_weather);
        btnSeeWeather.setEnabled(!editTextCityName.getText().toString().isEmpty());
        btnSeeWeather.setOnClickListener(v -> {
            Snackbar.make(view.findViewById(R.id.cities_list_fragment),
                        R.string.show_forecast_confirm, Snackbar.LENGTH_LONG).setAction(R.string.show_forecast_yes,
                        view1 -> openFragmentListener.openFragment(WeatherInfoFragment.newInstance(editTextCityName.getText().toString(), optionsAdapter.getData())))
                    .show();
        });

        optionsRecycler = view.findViewById(R.id.recycler);
    }

    private void setupRecycler(View view, List<OptionItem> data) {
        optionsAdapter = new OptionsAdapter(view.getContext(), data, (adapterView, v, i, l) -> { });
        optionsRecycler.setAdapter(optionsAdapter);
        optionsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void validate(TextView view, String message) {
        String value = view.getText().toString();

        if (value.isEmpty()) {
            view.setError(message);
            btnSeeWeather.setEnabled(false);
        } else {
            view.setError(null);
            btnSeeWeather.setEnabled(true);
        }
    }

//    private void showExtraInfoFragment() {
//        if (isExtraInfo && editTextCityName.getText() != null && !editTextCityName.getText().toString().isEmpty()) {
//            openFragmentListener.openFragment(R.id.fragment_extra_info,
//                    WeatherInfoFragment.newInstance(editTextCityName.getText().toString(), optionsAdapter.getData()));
//        }
//
//    }

}

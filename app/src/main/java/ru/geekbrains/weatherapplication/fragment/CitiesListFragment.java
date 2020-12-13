package ru.geekbrains.weatherapplication.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.weatherapplication.BuildConfig;
import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.adapter.OptionsAdapter;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.data.State;
import ru.geekbrains.weatherapplication.data.dto.CityListItem;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.utils.OpenFragmentListener;

import static ru.geekbrains.weatherapplication.data.Constants.CITY_LIST_FILE_PATH;
import static ru.geekbrains.weatherapplication.data.Constants.GET_WEATHER_URL;
import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;
import static ru.geekbrains.weatherapplication.data.Constants.WEATHER_OPTIONS;


public class CitiesListFragment extends Fragment {
    private static final String TAG = CitiesListFragment.class.getSimpleName();

    View mainFragment;
    private View viewLoading;
    private View viewError;
    private TextView textViewError;
    private Button btnOkError;

    private TextInputEditText editTextCityName;
    private Button btnSeeWeather;

    private OptionsAdapter optionsAdapter;
    private RecyclerView optionsRecycler;

    private OpenFragmentListener openFragmentListener;


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
        mainFragment = view.findViewById(R.id.weather_day_info);
        viewLoading = view.findViewById(R.id.frame_loading);
        viewError = view.findViewById(R.id.frame_error);
        textViewError = view.findViewById(R.id.text_view_error);
        btnOkError = view.findViewById(R.id.btn_ok_error);
        btnOkError.setOnClickListener(v -> showStateView(State.HasData));

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
                        R.string.show_forecast_confirm, Snackbar.LENGTH_LONG).setAction(R.string.show_forecast_yes, view1 -> loadWeatherData()).show();
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

    private void loadWeatherData(){
        showStateView(State.Loading);

        CityListItem city = findCityByName(editTextCityName.getText().toString());

        if (city != null) {
            if (DEBUG) {
                Log.d(TAG, "found city:" + city);
            }
            getWeather(city.getCoord().getLat(), city.getCoord().getLon());
        }
        else {
            if (DEBUG) {
                Log.e(TAG, "Not found city");
            }

            showStateView(State.ErrorData);
        }

    }

    private CityListItem findCityByName(String cityName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().getAssets().open(CITY_LIST_FILE_PATH)));

            List<CityListItem> cities = new Gson().fromJson(reader, new TypeToken<List<CityListItem>>() {}.getType());

            CityListItem res = cities.stream()
                    .filter(city -> cityName.equals(city.getName()))
                    .findAny().orElse(null);

            reader.close();
            return res;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void getWeather(float lat, float lon) {
        StringBuilder requestUrl = new StringBuilder(GET_WEATHER_URL);

        requestUrl.append("lat=").append(lat)
                .append("&lon="+ lon)
                .append("&appid=").append(BuildConfig.WEATHER_API_KEY);

        try {
            final URL uri = new URL(requestUrl.toString());
            final Handler handler = new Handler();

            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    if (DEBUG) {
                        Log.d(TAG, "Connection response code: : "+urlConnection.getResponseCode());
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = in.lines().collect(Collectors.joining("\n"));

                    if (DEBUG) {
                        Log.d(TAG, "Weather result: "+result);
                    }

                    // data to model
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);

                    // to main thread
                    handler.post(() -> handleWeather(weatherRequest));
                }
                catch (Exception e) {
                    Log.e(TAG, "getWeather request -> connection - failed", e);
                    e.printStackTrace();

                    handler.post(() -> handleError());
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            });

        } catch (MalformedURLException e) {
            if (DEBUG) {
                Log.e(TAG, "getWeather request -> create uri - failed");
            }
            e.printStackTrace();

            handleError();
        }
    }

    private void handleWeather(WeatherRequest weatherRequest){

        String cityName = weatherRequest.getName();
        float temp = weatherRequest.getMain().getTemp();

        showStateView(State.HasData);
        openFragmentListener.openFragment(WeatherInfoFragment.newInstance(weatherRequest));
    }

    private void handleError(){
        showStateView(State.ErrorData);
    }

    private void showStateView(@NonNull State state){
        switch (state){
            case HasData:
                viewLoading.setVisibility(View.GONE);
                viewError.setVisibility(View.GONE);

                mainFragment.setVisibility(View.VISIBLE);
                break;

            case Loading:
                viewError.setVisibility(View.GONE);
                mainFragment.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;

            case ErrorData:
                viewLoading.setVisibility(View.GONE);
                mainFragment.setVisibility(View.GONE);

                textViewError.setText(getText(R.string.cannot_load_info_error));
                viewError.setVisibility(View.VISIBLE);
                break;

            default:
                throw new IllegalArgumentException("Wrong state: " + state);
        }

    }

}

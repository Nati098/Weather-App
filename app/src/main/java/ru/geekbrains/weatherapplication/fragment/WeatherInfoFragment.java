package ru.geekbrains.weatherapplication.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.adapter.CurrentWeatherExtraAdapter;
import ru.geekbrains.weatherapplication.adapter.WeatherWeekAdapter;
import ru.geekbrains.weatherapplication.data.Constants;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.data.State;
import ru.geekbrains.weatherapplication.data.dto.CityListItem;
import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.request.CurrentWeatherRequest;
import ru.geekbrains.weatherapplication.data.request.MainRequest;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.data.request.WeekWeatherRequest;
import ru.geekbrains.weatherapplication.item.CurrentWeatherExtraItem;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.item.WeatherItem;
import ru.geekbrains.weatherapplication.service.ApiDataReceiver;

import static ru.geekbrains.weatherapplication.data.Constants.CITY_LIST_FILE_PATH;
import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;
import static ru.geekbrains.weatherapplication.data.Constants.WEATHER_OPTIONS;
import static ru.geekbrains.weatherapplication.data.Constants.getFormattedExtraInfo;


public class WeatherInfoFragment extends BaseFragment {
    private static final String TAG = WeatherInfoFragment.class.getSimpleName();

    private static final String ADDRESS_WEATHER = "https://www.gismeteo.ru/";

    private View mainFragment;
    private View viewLoading;

    private Toolbar toolbar;
    private TextView tempTextView;

    private ImageView imageWeather;

    private CurrentWeatherExtraAdapter extraInfoAdapter;
    private RecyclerView extraInfoRecycler;

    private WeatherWeekAdapter weatherWeekAdapter;
    private RecyclerView weatherWeekRecycler;

    private Button btnMoreInfo;

    private String cityName;
    private List<OptionItem> options;

    public static WeatherInfoFragment newInstance(String cityName, List<OptionItem> options) {
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER_OPTIONS, new Parcel(cityName, options));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);

        Parcel parcel = (Parcel) getArguments().getSerializable(WEATHER_OPTIONS);
        if (savedInstanceState != null) {
            parcel = (Parcel) savedInstanceState.getSerializable(WEATHER_OPTIONS);
        }

        cityName = parcel.cityName;
        options = parcel.options;
        loadWeatherData(findCityByName(parcel.cityName));

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void bindView(View view) {
        mainFragment = view.findViewById(R.id.weather_day_info);
        viewLoading = view.findViewById(R.id.frame_loading);

        toolbar = getActivity().findViewById(R.id.toolbar);

        tempTextView = getActivity().findViewById(R.id.current_temp);

        imageWeather = getActivity().findViewById(R.id.image_current_weather);

        weatherWeekRecycler = view.findViewById(R.id.weather_week_recycler);
        weatherWeekRecycler.setVisibility(View.GONE);
        extraInfoRecycler = view.findViewById(R.id.extra_info_recycler);

        btnMoreInfo = view.findViewById(R.id.btn_more_info);
        btnMoreInfo.setOnClickListener(v -> {
            Uri uri = Uri.parse(ADDRESS_WEATHER);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                getActivity().startActivity(intent);
            }
        });
    }

    private void setupRecycler(View view, List<CurrentWeatherExtraItem> options) {
        extraInfoAdapter = new CurrentWeatherExtraAdapter(view.getContext(), options, (adapterView, v, i, l) -> { });
        extraInfoRecycler.setAdapter(extraInfoAdapter);
        extraInfoRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        weatherWeekRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        weatherWeekAdapter = new WeatherWeekAdapter(view.getContext(), R.layout.weather_week_item_list,
                generateWeatherWeekList(), (adapterView, v, i, l) -> { });
        weatherWeekRecycler.setAdapter(weatherWeekAdapter);
        weatherWeekRecycler.setVisibility(options.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }

    private void loadWeatherData(CityListItem city) {
        showStateView(State.Loading);

        if (city != null) {
            if (DEBUG) {
                Log.d(TAG, "found city:" + city);
            }
            getWeather(city, 1);
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

    public void getWeather(CityListItem city, int requestMode) {
        new Thread(new ApiDataReceiver(this, this, city, requestMode)).start();
    }

    private void showStateView(@NonNull State state){
        switch (state){
            case HasData:
                viewLoading.setVisibility(View.GONE);

                mainFragment.setVisibility(View.VISIBLE);
                break;

            case Loading:
                mainFragment.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;

            case ErrorData:
                viewLoading.setVisibility(View.GONE);
                mainFragment.setVisibility(View.GONE);

                createAlertDialog(getText(R.string.cannot_load_info_error));
                break;

            default:
                throw new IllegalArgumentException("Wrong state: " + state);
        }

    }

    private void createAlertDialog(CharSequence errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.error_title)
                .setMessage(errorMsg)
                .setIcon(R.drawable.ic_zip)
                .setCancelable(true)
                .setNegativeButton(R.string.button_retry,
                        (dialog, id) -> loadWeatherData(findCityByName(cityName)))
                .setPositiveButton(R.string.button_back, (dialog, id) -> getActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof ApiDataReceiver) {
            MainRequest mainReq = ((ApiDataReceiver) o).getWeatherRequest();

            WeatherRequest weatherRequest = (WeatherRequest) mainReq;
            updateCurrentWeather(cityName,
                    weatherRequest.getFirstWeather().getIcon(),
                    getString(R.string.weather_info_title, String.format("%f2", weatherRequest.getMain().getTemp())));

            setupRecycler(getView(), prepareListOptions(weatherRequest.getCurrent()));
            btnMoreInfo.setEnabled(true);
        }

    }

    public void updateCurrentWeather(String title, String icon, String temp) {
        toolbar.setTitle(title);
        tempTextView.setText(temp);
        imageWeather.setImageResource(Constants.getWeatherImage(icon));
    }

    private List<CurrentWeatherExtraItem> prepareListOptions(CurrentWeather newData) {
        List<CurrentWeatherExtraItem> data = new ArrayList<>();

        options.forEach(optionItem -> {
            if (optionItem.isActive()) {
                data.add(new CurrentWeatherExtraItem(R.drawable.ic_temp_normal,
                        optionItem.getLabel(), getFormattedExtraInfo(optionItem, newData)));
            }
        });

        return data;
    }

    public List<WeatherItem> generateWeatherWeekList() {
        List<WeatherItem> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            data.add(new WeatherItem(getString(R.string.sunday), R.drawable.ic_cloudy, i+7));
        }
        return data;
    }

    @Override
    void updateView(MainRequest data) {

    }

}

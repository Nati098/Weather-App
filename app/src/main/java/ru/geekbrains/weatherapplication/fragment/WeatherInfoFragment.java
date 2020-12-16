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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.adapter.CurrentWeatherExtraAdapter;
import ru.geekbrains.weatherapplication.adapter.WeatherWeekAdapter;
import ru.geekbrains.weatherapplication.data.Constants;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.item.CurrentWeatherExtraItem;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.item.WeatherItem;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;
import static ru.geekbrains.weatherapplication.data.Constants.WEATHER_OPTIONS;


public class WeatherInfoFragment extends Fragment {
    private static final String TAG = WeatherInfoFragment.class.getSimpleName();

    private static final String ADDRESS_WEATHER = "https://www.gismeteo.ru/";

    private Toolbar toolbar;
    private TextView tempTextView;

    private ImageView image_weather;

    private CurrentWeatherExtraAdapter extraInfoAdapter;
    private RecyclerView extraInfoRecycler;

    private WeatherWeekAdapter weatherWeekAdapter;
    private RecyclerView weatherWeekRecycler;

    private Button btnMoreInfo;


    public static WeatherInfoFragment newInstance(String cityName, List<OptionItem> options) {
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER_OPTIONS, new Parcel(cityName, options));
        fragment.setArguments(args);
        return fragment;
    }

    public static WeatherInfoFragment newInstance(WeatherRequest weatherRequest) {
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        Bundle args = new Bundle();

        String icon = "00d";
        if (weatherRequest.getWeather() != null) {
            icon = weatherRequest.getWeather()[0].getIcon();
        }

        args.putSerializable(WEATHER_OPTIONS, new Parcel(weatherRequest.getName(), new ArrayList<>(), icon));
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

        if (parcel == null) {
            Toast.makeText(getContext(), getString(R.string.cannot_load_info_error), Toast.LENGTH_LONG);
        }
        else {
            String title = getString(R.string.weather_info_title, parcel.cityName);
            if (DEBUG) {
                Log.d("WeatherInfoFragment", "title = " + title);
            }
            toolbar.setTitle(title);

            image_weather.setImageResource(Constants.getWeatherImage(parcel.icon));

            setupRecycler(view, parcel.options);
            btnMoreInfo.setEnabled(true);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void bindView(View view) {
        toolbar = getActivity().findViewById(R.id.toolbar);

        tempTextView = getActivity().findViewById(R.id.current_temp);

        image_weather = getActivity().findViewById(R.id.image_current_weather);

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

    public void updateView(WeatherRequest weatherRequest) {
        toolbar.setTitle(weatherRequest.getName());

        String title = getString(R.string.weather_info_title, String.format("%f2", weatherRequest.getMain().getTemp()));
        tempTextView.setText(title);
    }

    private void setupRecycler(View view, List<OptionItem> options) {
        extraInfoAdapter = new CurrentWeatherExtraAdapter(view.getContext(), generateExtraInfoList(options), (adapterView, v, i, l) -> { });
        extraInfoRecycler.setAdapter(extraInfoAdapter);
        extraInfoRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        weatherWeekRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        weatherWeekAdapter = new WeatherWeekAdapter(view.getContext(), R.layout.weather_week_item_list,
                generateWeatherWeekList(), (adapterView, v, i, l) -> { });
        weatherWeekRecycler.setAdapter(weatherWeekAdapter);
    }

    public List<CurrentWeatherExtraItem> generateExtraInfoList(List<OptionItem> options) {
        List<CurrentWeatherExtraItem> data = new ArrayList<>();

        options.forEach(optionItem -> {
            if (optionItem.isActive()) {
                data.add(new CurrentWeatherExtraItem(R.drawable.ic_temp_normal,
                        optionItem.getLabel(),
                        String.format("%d%s", 7,getString(R.string.temperature_unit))));
            }
        });

        return data;
    }

    public List<WeatherItem> generateWeatherDayList() {
        List<WeatherItem> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            data.add(new WeatherItem(getString(R.string.time_dummy), R.drawable.ic_rainy_moon, i+7));
        }
        return data;
    }

    public List<WeatherItem> generateWeatherWeekList() {
        List<WeatherItem> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            data.add(new WeatherItem(getString(R.string.sunday), R.drawable.ic_cloudy, i+7));
        }
        return data;
    }
}

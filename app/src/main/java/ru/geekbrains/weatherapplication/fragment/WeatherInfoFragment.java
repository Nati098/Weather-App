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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.weatherapplication.BaseApp;
import ru.geekbrains.weatherapplication.BuildConfig;
import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.adapter.CurrentWeatherExtraAdapter;
import ru.geekbrains.weatherapplication.adapter.WeatherWeekAdapter;
import ru.geekbrains.weatherapplication.data.Constants;
import ru.geekbrains.weatherapplication.data.Parcel;
import ru.geekbrains.weatherapplication.data.State;
import ru.geekbrains.weatherapplication.data.dto.CityListItem;
import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.DailyWeather;
import ru.geekbrains.weatherapplication.data.request.MainRequest;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.data.request.WeekWeatherRequest;
import ru.geekbrains.weatherapplication.item.CurrentWeatherExtraItem;
import ru.geekbrains.weatherapplication.item.OptionItem;
import ru.geekbrains.weatherapplication.item.WeatherItem;
import ru.geekbrains.weatherapplication.room.CityEntity;
import ru.geekbrains.weatherapplication.service.ApiService;
import ru.geekbrains.weatherapplication.service.RetrofitClientImpl;

import static ru.geekbrains.weatherapplication.data.Constants.ABSOLUTE_ZERO;
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

    private void setupRecycler(View view, List<CurrentWeatherExtraItem> options, List<WeatherItem>weatherWeekList) {
        extraInfoAdapter = new CurrentWeatherExtraAdapter(view.getContext(), options, (adapterView, v, i, l) -> { });
        extraInfoRecycler.setAdapter(extraInfoAdapter);
        extraInfoRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        extraInfoRecycler.setVisibility(options.isEmpty() ? View.INVISIBLE : View.VISIBLE);

        weatherWeekRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        weatherWeekAdapter = new WeatherWeekAdapter(view.getContext(), R.layout.weather_week_item_list,
                weatherWeekList, (adapterView, v, i, l) -> { });
        weatherWeekRecycler.setAdapter(weatherWeekAdapter);
        weatherWeekRecycler.setVisibility(weatherWeekList.isEmpty() ? View.INVISIBLE : View.VISIBLE);
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
        CityEntity cityEntity = BaseApp.getInstance().getCityDao().getCityByName(cityName);
        if (cityEntity != null) {
            BaseApp.getInstance().getCityDao().updateTempr(cityEntity.id, 0.0);
            return (new CityListItem()).convertFromCityEntity(cityEntity);
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().getAssets().open(CITY_LIST_FILE_PATH)));

            List<CityListItem> cities = new Gson().fromJson(reader, new TypeToken<List<CityListItem>>() {}.getType());

            CityListItem res = cities.stream()
                    .filter(city -> cityName.equals(city.getName()))
                    .findAny().orElse(null);

            reader.close();

            saveToDb(res);
            return res;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void saveToDb(CityListItem city) {
        CityEntity entity = new CityEntity();
        entity.id = city.getId();
        entity.name = city.getName();
        entity.state = city.getState();
        entity.country = city.getCountry();
        entity.lon = city.getCoord().getLon();
        entity.lat = city.getCoord().getLat();
        entity.tempr = 0.0;

        BaseApp.getInstance().getCityDao().insertCity(entity);

        if (DEBUG) {
            Log.d(TAG, "saved city " + city.getName() + " to db");
        }
    }

    public void getWeather(CityListItem city, int requestMode) {
        StringBuilder excluded = new StringBuilder();
        switch (requestMode) {
            case 1:
                excluded.append(Exclude.MINUTELY.description).append(",")
                        .append(Exclude.HOURLY.description).append(",")
                        .append(Exclude.ALERTS.description);
                break;
            default:
                excluded.append(Exclude.MINUTELY.description).append(",")
                        .append(Exclude.HOURLY.description).append(",")
                        .append(Exclude.DAILY.description).append(",")
                        .append(Exclude.ALERTS.description);
                break;
        }

        ApiService service = RetrofitClientImpl.getInstance().create(ApiService.class);
        service.getWeekWeather(city.getCoord().getLat(), city.getCoord().getLon(), excluded.toString(), BuildConfig.WEATHER_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<WeekWeatherRequest>() {
                    @Override
                    public void onSuccess(WeekWeatherRequest weatherRequest) {
                        BaseApp.getInstance().getCityDao().updateTempr(city.getId(), weatherRequest.getCurrent().getTemp());

                        updateView(weatherRequest);
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showStateView(State.ErrorData);
                        Log.e(TAG, e.toString());
                        dispose();
                    }
                });


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
        Log.d(TAG, "UPDATE UPDATE UPDATE");
    }

    public void updateCurrentWeather(String title, String icon, String temp) {
        toolbar.setTitle(title);
        tempTextView.setText(temp+" "+getString(R.string.temperature_unit));
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

    public List<WeatherItem> prepareWeatherWeekList(ArrayList<DailyWeather> newData) {
        List<WeatherItem> data = new ArrayList<>();

        if (newData == null) {
            return data;
        }

        for (int i = 0; i < newData.size(); i++) {
            data.add(new WeatherItem(getString(WeekDay.findByKey(i)),
                    Constants.getWeatherImage(newData.get(i).getWeather().get(0).getIcon()),
                    (int) (newData.get(i).getTemp().getDay()-ABSOLUTE_ZERO)));
        }
        return data;
    }

    @Override
    void updateView(MainRequest data) {
        showStateView(State.HasData);
        if (DEBUG) {
            Log.d(TAG, "updateView data: "+data);
        }
        WeatherRequest weatherRequest = (WeatherRequest) data;

        updateCurrentWeather(getString(R.string.weather_info_title, cityName),
                weatherRequest.getFirstWeather().getIcon(),
                String.format("%.0f", weatherRequest.getCurrent().getTemp()-ABSOLUTE_ZERO)
        );

        setupRecycler(getView(), prepareListOptions(weatherRequest.getCurrent()), prepareWeatherWeekList(weatherRequest.getDaily()));
        btnMoreInfo.setEnabled(true);
    }


    private enum Exclude {
        CURRENT("current"),
        MINUTELY("minutely"),
        HOURLY("hourly"),
        DAILY("daily"),
        ALERTS("alerts");


        private final String description;

        Exclude(String description) {
            this.description = description;
        }

        public String toStr() {
            return description;
        }
    }

    public enum WeekDay {
        MONDAY(0, R.string.monday),
        TUESDAY(1, R.string.tuesday),
        WEDNESDAY(2, R.string.wednesday),
        THURSDAY(3, R.string.thursday),
        FRIDAY(4, R.string.friday),
        SATURDAY(5, R.string.saturday),
        SUNDAY(6, R.string.sunday);

        private int number;
        private int stringCode;

        WeekDay(int number, int stringCode) {
            this.number = number;
            this.stringCode = stringCode;
        }

        public int getCode() {
            return stringCode;
        }

        public static int findByKey(int i) {
            WeekDay[] weekDays = WeekDay.values();
            int buf = i % 7;
            for (WeekDay day : weekDays) {
                if (day.number == buf) {
                    return day.getCode();
                }
            }
            return 0;
        }
    }
}

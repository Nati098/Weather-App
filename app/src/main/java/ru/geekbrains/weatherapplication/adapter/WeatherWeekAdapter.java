package ru.geekbrains.weatherapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.item.WeatherItem;


public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekAdapter.WeatherWeekViewHolder> {

    private Context context;
    private int layout;
    private List<WeatherItem> data;
    private AdapterView.OnItemClickListener listener;


    public WeatherWeekAdapter(Context context, int layout, List<WeatherItem> data, AdapterView.OnItemClickListener listener) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherWeekViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherWeekViewHolder holder, int position) {
        holder.bind(context, data.get(position));
    }

    @Override
    public int getItemCount() {
        return 7;
    }


    public static class WeatherWeekViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        public TextView timePoint;
        public TextView temperature;

        private View view;

        public WeatherWeekViewHolder(@NonNull View view) {
            super(view);
            this.view = view;

            img = view.findViewById(R.id.weather_img);
            timePoint = view.findViewById(R.id.time_point_text);
            temperature = view.findViewById(R.id.temperature_text);
        }

        public void bind(Context context, WeatherItem item) {
            timePoint.setText(item.getTimePoint());
            img.setImageResource(item.getImgId());
            temperature.setText(String.format("%d%s", item.getTemp(), context.getString(R.string.temperature_unit)));

        }
    }

}

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
import ru.geekbrains.weatherapplication.data.CurrentWeatherExtra;


public class CurrentWeatherExtraAdapter extends RecyclerView.Adapter<CurrentWeatherExtraAdapter.CurrentWeatherExtraViewHolder> {

    private Context context;
    private List<CurrentWeatherExtra> data;
    private AdapterView.OnItemClickListener listener;


    public CurrentWeatherExtraAdapter(Context context, List<CurrentWeatherExtra> data, AdapterView.OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CurrentWeatherExtraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrentWeatherExtraViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.extra_info_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentWeatherExtraViewHolder holder, int position) {
        holder.bind(context, data.get(0));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class CurrentWeatherExtraViewHolder extends RecyclerView.ViewHolder {

        private ImageView infoImg;
        public TextView label;
        public TextView value;

        private View view;

        public CurrentWeatherExtraViewHolder(@NonNull View view) {
            super(view);
            this.view = view;

            infoImg = view.findViewById(R.id.info_img);
            label = view.findViewById(R.id.info_label);
            value = view.findViewById(R.id.info_value);

        }

        public void bind(Context context, CurrentWeatherExtra item) {
            infoImg.setImageResource(item.getImgId());
            label.setText(item.getLabel());
            value.setText(item.getValue());

        }
    }



}

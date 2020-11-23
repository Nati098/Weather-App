package ru.geekbrains.weatherapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.geekbrains.weatherapplication.R;
import ru.geekbrains.weatherapplication.SettingsActivity;
import ru.geekbrains.weatherapplication.data.SystemPreferences;
import ru.geekbrains.weatherapplication.item.OptionItem;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;


public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {

    private Context context;
    private List<OptionItem> data;
    private AdapterView.OnItemClickListener listener;


    public OptionsAdapter(Context context, List<OptionItem> data, AdapterView.OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionsAdapter.OptionsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.options_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, int position) {
        holder.bind(context, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<OptionItem> getData() {
        return data;
    }

    public class OptionsViewHolder extends RecyclerView.ViewHolder {

        public TextView label;
        public SwitchCompat activateOptionCheckBox;

        private View view;

        public OptionsViewHolder(@NonNull View view) {
            super(view);
            this.view = view;

            label = view.findViewById(R.id.option_label);
            activateOptionCheckBox = view.findViewById(R.id.activate_option_checkbox);
        }

        public void bind(Context context, OptionItem item) {
            if (DEBUG) {
                Log.d("OptionsAdapter", "item isActive " + item.isActive());
            }
            label.setText(item.getLabel());
            activateOptionCheckBox.setChecked(item.isActive());
            activateOptionCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                item.setActive(isChecked);
                SystemPreferences.setPreference(item.getId(), isChecked);
            });
        }
    }
}

package ru.geekbrains.weatherapplication.fragment;

import androidx.fragment.app.Fragment;

import ru.geekbrains.weatherapplication.data.request.MainRequest;


public abstract class BaseFragment extends Fragment {
    abstract void updateView(MainRequest data);
}

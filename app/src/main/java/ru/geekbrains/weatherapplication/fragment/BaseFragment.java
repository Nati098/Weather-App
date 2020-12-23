package ru.geekbrains.weatherapplication.fragment;

import androidx.fragment.app.Fragment;

import java.util.Observer;

import ru.geekbrains.weatherapplication.data.request.MainRequest;


public abstract class BaseFragment extends Fragment implements Observer {
    abstract void updateView(MainRequest data);
}

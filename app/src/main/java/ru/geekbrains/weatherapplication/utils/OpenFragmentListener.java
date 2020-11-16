package ru.geekbrains.weatherapplication.utils;

import androidx.fragment.app.Fragment;

public interface OpenFragmentListener {

    void openFragment(Fragment fragment);
    void openFragment(int id, Fragment fragment);

}

package ru.geekbrains.weatherapplication.item;

public class OptionItem {
    private String label;
    private boolean isActive;


    public OptionItem(String label, boolean isActive) {
        this.label = label;
        this.isActive = isActive;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return isActive;
    }
}

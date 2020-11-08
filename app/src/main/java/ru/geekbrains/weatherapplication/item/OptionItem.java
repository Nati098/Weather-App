package ru.geekbrains.weatherapplication.item;

public class OptionItem {
    private String id = "";
    private String label;
    private boolean isActive;

    public OptionItem(String id, String label, boolean isActive) {
        this.label = label;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

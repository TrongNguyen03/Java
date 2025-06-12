package org.example;

public class WeatherInfo {
    public String description;
    public double temp;
    public int humidity;
    public String iconCode;

    public WeatherInfo(String description, double temp, int humidity, String iconCode) {
        this.description = description;
        this.temp = temp;
        this.humidity = humidity;
        this.iconCode = iconCode;
    }

    public String getIconUrl() {
        return "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
    }
}

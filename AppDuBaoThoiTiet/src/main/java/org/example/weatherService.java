package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class weatherService {
    private static final String API_KEY = "822f3da836f839d603dcbb215a42f6c8";

    public static WeatherInfo getWeather(String city) {
        try {
            String endpoint = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + API_KEY + "&units=metric&lang=vi";
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner sc = new Scanner(conn.getInputStream());
            StringBuilder inline = new StringBuilder();
            while (sc.hasNext()) {
                inline.append(sc.nextLine());
            }
            sc.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inline.toString());

            String description = node.get("weather").get(0).get("description").asText();
            String iconCode = node.get("weather").get(0).get("icon").asText();
            double temp = node.get("main").get("temp").asDouble();
            int humidity = node.get("main").get("humidity").asInt();

            return new WeatherInfo(description, temp, humidity, iconCode);
        } catch (IOException e) {
            return null;
        }
    }
}

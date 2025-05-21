package org.example.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Result;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ResultApi {
    private static final String BASE_URL = "http://localhost:8080/api/results";

    public static List<Result> getAllResults() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.toString(), new TypeReference<List<Result>>() {});
            } else {
                System.err.println("Lỗi lấy kết quả: " + conn.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

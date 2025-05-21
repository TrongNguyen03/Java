package org.example.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Result;
import org.example.model.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ResultApi {
    private static final String BASE_URL = "http://localhost:8080/api/results";

    // ✅ Gửi kết quả lên server
    public static boolean submitResult(Result result) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            var node = mapper.createObjectNode();
            node.put("userId", result.getUserId());
            node.put("score", result.getScore());
            node.put("submitTime", result.getSubmitTime().toString());

            String json = mapper.writeValueAsString(node);

            conn.getOutputStream().write(json.getBytes());

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println("Gửi kết quả thất bại, mã lỗi: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


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
                mapper.registerModule(new JavaTimeModule());
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

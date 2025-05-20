package org.example.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Question;
import okhttp3.*;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class QuizApi {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api/questions";
    private static final Gson gson = new Gson();

    public static List<Question> getRandomQuestions(int number) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/random?limit=" + number)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type listType = new TypeToken<List<Question>>() {}.getType();
                List<Question> allQuestions = gson.fromJson(response.body().string(), listType);

                // Lọc bỏ câu hỏi có options null hoặc ít hơn 4 lựa chọn
                allQuestions.removeIf(q -> q.getOptions() == null || q.getOptions().size() < 4);

                return allQuestions;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}


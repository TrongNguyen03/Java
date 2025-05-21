package org.example.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.example.model.Question;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class QuizApi {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api/questions";
    private static final Gson gson = new Gson();

    // Lấy danh sách tất cả câu hỏi
    public static List<Question> getAllQuestions() {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type listType = new TypeToken<List<Question>>() {}.getType();
                return gson.fromJson(response.body().string(), listType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    // Lấy câu hỏi theo ID
    public static Question getQuestionById(Long id) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), Question.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm câu hỏi mới
    public static boolean createQuestion(Question question) {
        String json = gson.toJson(question);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật câu hỏi
    public static boolean updateQuestion(Long id, Question question) {
        String json = gson.toJson(question);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa câu hỏi
    public static boolean deleteQuestion(Long id) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Câu hỏi ngẫu nhiên
    public static List<Question> getRandomQuestions(int number) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/random?limit=" + number)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type listType = new TypeToken<List<Question>>() {}.getType();
                List<Question> allQuestions = gson.fromJson(response.body().string(), listType);

                allQuestions.removeIf(q -> q.getOptions() == null || q.getOptions().size() < 4);

                return allQuestions;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}

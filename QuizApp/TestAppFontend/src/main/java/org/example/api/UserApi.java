package org.example.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.User;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserApi {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private static final Gson gson = new Gson();

    // Đăng nhập
    public static User login(String username, String password) {
        try {
            String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(BASE_URL + "/login")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm người dùng
    public static User addUser(User user) {
        try {
            String json = gson.toJson(user);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(BASE_URL + "/register")  // POST tới /api/auth/register
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return gson.fromJson(responseBody, User.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Cập nhật thông tin người dùng
    public static boolean updateUser(User user) {
        try {
            String json = gson.toJson(user);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + user.getId())
                    .put(body)
                    .build();

            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xoá người dùng
    public static boolean deleteUser(Long id) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + id)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<User> getAllUsers() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/users")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Type listType = new TypeToken<List<User>>(){}.getType();
                return gson.fromJson(json, listType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}

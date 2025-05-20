package org.example.api;

import com.google.gson.Gson;
import org.example.model.User;
import okhttp3.*;

public class UserApi {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private static final Gson gson = new Gson();

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
}


package com.example.mua_ban_xe_cu.Model.Response;

import com.google.gson.annotations.SerializedName;
import com.example.mua_ban_xe_cu.database.User;

public class LoginResponse {

    @SerializedName("message") // Trường này ánh xạ đến thông báo phản hồi
    private String message;

    @SerializedName("token") // Token trả về từ server
    private String token;

    @SerializedName("user") // Đối tượng User trả về từ server
    private User user;

    // Getters và Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.example.mua_ban_xe_cu.API;

public class AppConfig {
    // Cập nhật BASE_URL để trỏ đến API Gateway, ví dụ: http://your_server_ip:8080/
    private static String BASE_URL = "http://172.21.16.77:8080/";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String newBaseUrl) {
        BASE_URL = newBaseUrl;
    }
}

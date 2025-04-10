package com.example.mua_ban_xe_cu.API;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static Retrofit retrofit;

    // Phương thức này sẽ lấy IP từ AppConfig mỗi khi cần tạo Retrofit instance mới
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)  // Thời gian kết nối tối đa là 30 giây
                    .readTimeout(30, TimeUnit.SECONDS)     // Thời gian đọc dữ liệu tối đa là 30 giây
                    .writeTimeout(30, TimeUnit.SECONDS)    // Thời gian ghi dữ liệu tối đa là 30 giây
                    .build();

            // Khởi tạo Retrofit với URL lấy từ AppConfig
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.getBaseUrl())  // Sử dụng IP từ AppConfig
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}

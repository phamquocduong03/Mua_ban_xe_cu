package com.example.mua_ban_xe_cu.API;

import com.example.mua_ban_xe_cu.Model.Province;
import com.example.mua_ban_xe_cu.Model.Response.LoginResponse;
import com.example.mua_ban_xe_cu.Model.Response.PostCarResponse;
import com.example.mua_ban_xe_cu.Model.Response.RegisterResponse;
import com.example.mua_ban_xe_cu.Model.Response.UploadResponse;
import com.example.mua_ban_xe_cu.database.Car;
import com.example.mua_ban_xe_cu.database.Message;
import com.example.mua_ban_xe_cu.database.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Tỉnh thành từ API ngoài (nếu có sử dụng)
    @GET("api/?depth=2")
    Call<List<Province>> getProvinces();

    // ==== CAR SERVICE (prefix: /car) ====
    @Multipart
    @POST("car/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @POST("car/post_car")
    Call<PostCarResponse> postCar(@Body Car car);

    @GET("car/cars")
    Call<List<Car>> getCars();

    @PUT("car/delete_car/{carId}")
    Call<Car> deleteCar(@Path("carId") String carId);
    // ==== AUTH SERVICE (prefix: /auth) ====
    @POST("auth/register")
    Call<RegisterResponse> registerUser(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body User user);

    @GET("auth/getUserByUsername")
    Call<User> getUserByUsername(@Query("username") String username);

    // ==== CHAT SERVICE (prefix: /chat) ====
    @POST("chat/send_message")
    Call<Void> sendMessage(@Body Message message);

    @GET("chat/get_messages")
    Call<List<Message>> getMessages(@Query("sender") String sender,
                                    @Query("receiver") String receiver,
                                    @Query("carId") String carId);


    // Endpoint mới: lấy tất cả tin nhắn liên quan đến người dùng (cả gửi và nhận)
    @GET("chat/get_all_messages")
    Call<List<Message>> getAllMessages(@Query("userEmail") String userEmail);
    // Thêm endpoint PUT để xóa bài đăng

    @PUT("car/delete_by_image")
    Call<Car> deleteCarByImage(@Body Car car);

    @PUT("car/update_car")
    Call<Car> updateCar(@Body Car car);
    // Endpoint cập nhật thông tin tài khoản (auth service)
    @PUT("auth/update_user")
    Call<User> updateUser(@Body User user);



}

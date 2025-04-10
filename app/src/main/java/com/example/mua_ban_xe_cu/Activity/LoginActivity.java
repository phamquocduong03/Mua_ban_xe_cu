package com.example.mua_ban_xe_cu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.API.AppConfig;
import com.example.mua_ban_xe_cu.Model.Response.LoginResponse;
import com.example.mua_ban_xe_cu.database.User;
import com.example.mua_ban_xe_cu.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button btnLogin;
    private TextView linkRegister;

    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Sử dụng AppConfig để lấy URL máy chủ
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getBaseUrl())  // Sử dụng URL lấy từ AppConfig
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Khởi tạo các View
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        linkRegister = findViewById(R.id.linkRegister);

        // Handle login action
        btnLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Check if username and password are not empty
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Truy vấn MongoDB để lấy người dùng dựa trên username
            apiService.loginUser(new User(username, password, null, null, null, null,null , false))
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                LoginResponse loginResponse = response.body();

                                // Lấy thông tin từ phản hồi
                                String token = loginResponse.getToken();
                                User user = loginResponse.getUser();

                                if (user != null) {
                                    Log.d("LoginActivity", "Token: " + token);
                                    Log.d("LoginActivity", "User: " + user.toString());

                                    // Lưu thông tin vào SharedPreferences
                                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("user_token", token);
                                    editor.putString("user_email", user.getEmail());
                                    editor.putString("user_username", user.getUsername());
                                    editor.putString("phoneNumber", user.getPhoneNumber());
                                    editor.putString("user_province", user.getProvince());
                                    editor.putString("user_dob", user.getDob());            // Thêm dòng này
                                    editor.putString("facebookLink", user.getFacebookLink()); // Thêm dòng này
                                    editor.apply();


                                    // Chuyển đến MainActivity
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        });

        // Navigate to RegisterActivity
        linkRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}

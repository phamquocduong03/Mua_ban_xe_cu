package com.example.mua_ban_xe_cu.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatSpinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.API.AppConfig;
import com.example.mua_ban_xe_cu.Model.Province;
import com.example.mua_ban_xe_cu.Model.Response.RegisterResponse;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.User;

import java.lang.reflect.Field;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextConfirmPassword, editTextEmail, editTextFacebook;
    private Button buttonRegister;
    private AppCompatSpinner spinnerProvince; // Sử dụng AppCompatSpinner
    private EditText editTextPhoneNumber;

    private Retrofit retrofit;
    private ApiService apiService;

    private String selectedProvinceName;  // Province được chọn từ Spinner

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo các View (đã loại bỏ trường nhập ngày sinh)
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFacebook = findViewById(R.id.editTextFacebook);
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        spinnerProvince = findViewById(R.id.spinnerProvince);

        // Khởi tạo Retrofit để gọi API đăng ký người dùng
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Khởi tạo Retrofit để gọi API lấy danh sách tỉnh thành từ https://provinces.open-api.vn/
        Retrofit provincesRetrofit = new Retrofit.Builder()
                .baseUrl("https://provinces.open-api.vn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService provincesApiService = provincesRetrofit.create(ApiService.class);

        // Lấy danh sách tỉnh thành
        getProvinces(provincesApiService);

        // Thiết lập sự kiện cho nút đăng ký
        buttonRegister.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String province = selectedProvinceName;
            String facebookLink = editTextFacebook.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();

            // Kiểm tra các trường bắt buộc (loại bỏ trường ngày sinh)
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu khớp
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng User, truyền chuỗi rỗng cho trường dob (ngày sinh)
            User newUser = new User(name, username, password, email, province, facebookLink, "", phoneNumber, false);

            // Gửi yêu cầu đăng ký
            registerUserToMongoDB(newUser);
        });
    }

    @Override
    protected void onPause() {
        // Dùng reflection để đóng popup của Spinner nếu đang mở nhằm tránh lỗi WindowLeaked
        if (spinnerProvince != null) {
            try {
                Field popupField = spinnerProvince.getClass().getDeclaredField("mPopup");
                popupField.setAccessible(true);
                Object popup = popupField.get(spinnerProvince);
                if (popup != null && popup instanceof android.widget.ListPopupWindow) {
                    ((android.widget.ListPopupWindow) popup).dismiss();
                }
            } catch (Exception e) {
                // Nếu không thành công, in log hoặc bỏ qua
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    private void getProvinces(ApiService provincesApiService) {
        Call<List<Province>> call = provincesApiService.getProvinces();
        call.enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.isSuccessful()) {
                    List<Province> provinces = response.body();
                    if (provinces != null) {
                        ArrayAdapter<Province> provinceAdapter = new ArrayAdapter<>(RegisterActivity.this,
                                android.R.layout.simple_spinner_item, provinces);
                        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerProvince.setAdapter(provinceAdapter);

                        spinnerProvince.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                Province selectedProvince = (Province) parent.getItemAtPosition(position);
                                selectedProvinceName = selectedProvince.getName();
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                                // Không làm gì
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Danh sách tỉnh thành rỗng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Lỗi khi lấy tỉnh thành", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUserToMongoDB(User user) {
        apiService.registerUser(user).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Trở về màn hình đăng nhập
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại! Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

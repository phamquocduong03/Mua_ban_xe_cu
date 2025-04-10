package com.example.mua_ban_xe_cu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mua_ban_xe_cu.API.ApiClient;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.Adapter.CarAdapter;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.Car;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<Car> carList = new ArrayList<>();
    private ApiService apiService;

    // Các button dưới cùng
    private ImageButton btnHome, btnPostCar, btnAccount, btnMessages;
    private TextView btnLogin, btnRegister;
    private View loginLayout, menuLayout;

    private String currentUserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo view
        recyclerView = findViewById(R.id.recyclerViewCars);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loginLayout = findViewById(R.id.loginLayout);
        menuLayout = findViewById(R.id.menuLayout);
        btnHome = findViewById(R.id.btnHome);
        btnPostCar = findViewById(R.id.btnPostCar);
        btnAccount = findViewById(R.id.btnAccount);
        btnMessages = findViewById(R.id.btnMessages); // Nút mới cho phần tin nhắn

        // Cấu hình Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        // LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Kiểm tra người dùng đã đăng nhập chưa
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = preferences.getString("user_token", null);
        currentUserEmail = preferences.getString("user_email", "");

        if (token != null && !token.isEmpty()) {
            updateUIForLoggedInUser();
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            menuLayout.setVisibility(View.GONE);
        }

        // Lấy danh sách xe
        loadCars();

        // Bắt sự kiện nút login/register
        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        // Bắt sự kiện menu dưới
        btnHome.setOnClickListener(v -> recreate());
        btnPostCar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PostCarActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AccountActivity.class)));

        btnMessages.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MessagesActivity.class)));
    }

    private void updateUIForLoggedInUser() {
        loginLayout.setVisibility(View.GONE);
        menuLayout.setVisibility(View.VISIBLE);
    }

    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user_token");
        editor.remove("user_email");
        editor.apply();

        loginLayout.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
    }

    private void loadCars() {
        apiService.getCars().enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList.clear();

                    // Lọc bài đăng không phải của chính mình
                    for (Car car : response.body()) {
                        if (!car.getCreatedBy().equalsIgnoreCase(currentUserEmail)) {
                            carList.add(car);
                        }
                    }

                    // isOwner = false vì đây là màn hình chính, không phải bài đăng của mình
                    carAdapter = new CarAdapter(carList, apiService, false);
                    recyclerView.setAdapter(carAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Không có xe nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Log.e("RetrofitError", "Lỗi kết nối chi tiết: ", t); // ghi log stacktrace
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

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
        btnMessages = findViewById(R.id.btnMessages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Kiểm tra thông tin đăng nhập từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = preferences.getString("user_token", null);
        currentUserEmail = preferences.getString("user_email", "");

        if (token != null && !token.isEmpty()) {
            updateUIForLoggedInUser();
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            menuLayout.setVisibility(View.GONE);
        }

        // Khởi tạo ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        loadCars();

        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        btnHome.setOnClickListener(v -> recreate());
        btnPostCar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PostCarActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AccountActivity.class)));
        btnMessages.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MessagesActivity.class)));
    }

    private void updateUIForLoggedInUser() {
        loginLayout.setVisibility(View.GONE);
        menuLayout.setVisibility(View.VISIBLE);
    }

    private void loadCars() {
        apiService.getCars().enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList.clear();
                    for (Car car : response.body()) {
                        // Lọc bài đăng để loại trừ các bài đăng của chính user nếu cần (ví dụ, nếu không muốn hiển thị bài đăng của user)
                        if (!car.getCreatedBy().equalsIgnoreCase(currentUserEmail)) {
                            carList.add(car);
                        }
                    }
                    // Khởi tạo adapter mới: constructor sửa đổi chỉ nhận carList và boolean isOwner
                    carAdapter = new CarAdapter(carList, false);
                    recyclerView.setAdapter(carAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Không có xe nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

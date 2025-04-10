package com.example.mua_ban_xe_cu.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ManagePostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<Car> myCars = new ArrayList<>();
    private ApiService apiService;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_posts);

        recyclerView = findViewById(R.id.recyclerViewManagePosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient().create(ApiService.class);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserEmail = preferences.getString("user_email", "");

        loadMyPosts();
    }

    private void loadMyPosts() {
        apiService.getCars().enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Car> allCars = response.body();
                    myCars.clear();
                    for (Car car : allCars) {
                        if (car.getCreatedBy() != null
                                && car.getCreatedBy().equals(currentUserEmail)
                                && "Đã duyệt".equalsIgnoreCase(car.getStatus())) {
                            myCars.add(car);
                        }
                    }

                    if (myCars.isEmpty()) {
                        Toast.makeText(ManagePostsActivity.this, "Bạn chưa có bài đăng nào đang được duyệt!", Toast.LENGTH_SHORT).show();
                    }

                    carAdapter = new CarAdapter(myCars, apiService, true);
                    recyclerView.setAdapter(carAdapter);
                } else {
                    Toast.makeText(ManagePostsActivity.this, "Không lấy được bài đăng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(ManagePostsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

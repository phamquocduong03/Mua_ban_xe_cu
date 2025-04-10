package com.example.mua_ban_xe_cu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.API.AppConfig;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.Car;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarDetailActivity extends AppCompatActivity {
    private boolean isOwner = false;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        ImageView imageCar = findViewById(R.id.imageCar);
        TextView textCarName = findViewById(R.id.textCarName);
        TextView textCarPrice = findViewById(R.id.textCarPrice);
        TextView textCarOwner = findViewById(R.id.textCarOwner);
        TextView textCarAddress = findViewById(R.id.textCarAddress);
        TextView textCarPhone = findViewById(R.id.textCarPhone);
        TextView textCarEmail = findViewById(R.id.textCarEmail);
        Button buttonMessageSeller = findViewById(R.id.buttonMessageSeller);
        Button btnEditPost = findViewById(R.id.btnEditPost);
        Button btnDeletePost = findViewById(R.id.btnDeletePost);

        // Nhận dữ liệu từ Intent, bao gồm posterName đã lưu khi đăng bài
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String posterName = getIntent().getStringExtra("posterName");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phoneNumber");
        String email = getIntent().getStringExtra("email");
        String carId = getIntent().getStringExtra("carId");
        String brand = getIntent().getStringExtra("brand");
        String year = getIntent().getStringExtra("year");
        String type = getIntent().getStringExtra("type");
        isOwner = getIntent().getBooleanExtra("isOwner", false);

        textCarName.setText("Tên xe: " + name);
        textCarPrice.setText("Giá bán: " + price);
        // Hiển thị luôn posterName – nếu không có thì fallback sang email
        textCarOwner.setText("Người đăng: " + (posterName != null && !posterName.isEmpty() ? posterName : email));
        textCarAddress.setText("Địa chỉ: " + address);
        textCarPhone.setText("Số điện thoại: " + phone);
        textCarEmail.setText("Email: " + email);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            String updatedImageUrl = imageUrl.replace("http://localhost:5002/uploads/", AppConfig.getBaseUrl() + "car/uploads/");
            Glide.with(this).load(updatedImageUrl).into(imageCar);
        } else {
            Glide.with(this).load(R.drawable.ic_car_placeholder).into(imageCar);
        }

        if (isOwner) {
            buttonMessageSeller.setVisibility(View.GONE);
            btnEditPost.setVisibility(View.VISIBLE);
            btnDeletePost.setVisibility(View.VISIBLE);
        } else {
            buttonMessageSeller.setVisibility(View.VISIBLE);
            btnEditPost.setVisibility(View.GONE);
            btnDeletePost.setVisibility(View.GONE);
        }

        buttonMessageSeller.setOnClickListener(v -> {
            Intent intent = new Intent(CarDetailActivity.this, ChatActivity.class);
            intent.putExtra("receiver", email);
            intent.putExtra("carId", carId);
            startActivity(intent);
        });

        btnEditPost.setOnClickListener(v -> {
            Intent intent = new Intent(CarDetailActivity.this, EditCarActivity.class);
            intent.putExtra("carId", carId);
            intent.putExtra("name", name);
            intent.putExtra("price", price);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("address", address);
            intent.putExtra("phoneNumber", phone);
            intent.putExtra("email", email);
            intent.putExtra("brand", brand);
            intent.putExtra("year", year);
            intent.putExtra("type", type);
            // Chuyển thêm posterName nếu cần hiển thị trong EditCarActivity (nếu có)
            intent.putExtra("posterName", posterName);
            startActivity(intent);
        });

        btnDeletePost.setOnClickListener(v -> {
            Car carToDelete = new Car();
            carToDelete.setImageUrl(imageUrl); // Xóa dựa theo imageUrl

            apiService.deleteCarByImage(carToDelete).enqueue(new Callback<Car>() {
                @Override
                public void onResponse(Call<Car> call, Response<Car> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CarDetailActivity.this, "Bài đăng đã được xóa", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CarDetailActivity.this, AccountActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CarDetailActivity.this, "Không tìm thấy bài đăng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Car> call, Throwable t) {
                    Toast.makeText(CarDetailActivity.this, "Lỗi khi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

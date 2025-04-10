package com.example.mua_ban_xe_cu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mua_ban_xe_cu.R;

public class AccountActivity extends AppCompatActivity {

    private TextView tvName, tvUsername, tvEmail, tvPhone, tvProvince, tvFacebookLink;
    private Button btnEditAccount, btnQuanLyBaiDang, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvProvince = findViewById(R.id.tvProvince);
        tvFacebookLink = findViewById(R.id.tvFacebookLink);

        btnEditAccount = findViewById(R.id.btnEditAccount);
        btnQuanLyBaiDang = findViewById(R.id.btnQuanLyBaiDang);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = preferences.getString("user_name", "N/A");
        String username = preferences.getString("user_username", "N/A");
        String email = preferences.getString("user_email", "N/A");
        String phone = preferences.getString("phoneNumber", "N/A");
        String province = preferences.getString("user_province", "N/A");
        String facebookLink = preferences.getString("facebookLink", "N/A");

        // Hiển thị thông tin
        tvName.setText("Họ và tên: " + name);
        tvUsername.setText("Tên đăng nhập: " + username);
        tvEmail.setText("Email: " + email);
        tvPhone.setText("Số điện thoại: " + phone);
        tvProvince.setText("Tỉnh/Thành: " + province);
        tvFacebookLink.setText("Facebook: " + facebookLink);

        // Nút chỉnh sửa
        btnEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
            startActivity(intent);
        });

        // Nút quản lý bài đăng
        btnQuanLyBaiDang.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ManagePostsActivity.class);
            startActivity(intent);
        });

        // Nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}

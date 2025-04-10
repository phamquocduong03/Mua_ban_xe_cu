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

    private TextView tvUsername, tvEmail, tvPhone, tvProvince, tvDob, tvFacebookLink;
    private Button btnEditAccount,btnQuanLyBaiDang, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvProvince = findViewById(R.id.tvProvince);
        tvDob = findViewById(R.id.tvDob);
        tvFacebookLink = findViewById(R.id.tvFacebookLink);
        btnEditAccount = findViewById(R.id.btnEditAccount);
        btnQuanLyBaiDang = findViewById(R.id.btnQuanLyBaiDang);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy thông tin tài khoản từ SharedPreferences (đã lưu sau khi đăng nhập)
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = preferences.getString("user_username", "N/A");
        String email = preferences.getString("user_email", "N/A");
        String phone = preferences.getString("phoneNumber", "N/A");
        String province = preferences.getString("user_province", "N/A");
        String dob = preferences.getString("user_dob", "N/A");         // Nếu đã lưu ngày sinh
        String facebookLink = preferences.getString("facebookLink", "N/A"); // Nếu đã lưu link Facebook

        tvUsername.setText("Username: " + username);
        tvEmail.setText("Email: " + email);
        tvPhone.setText("Số điện thoại: " + phone);
        tvProvince.setText("Tỉnh/Thành: " + province);
        tvDob.setText("Ngày sinh: " + dob);
        tvFacebookLink.setText("Facebook: " + facebookLink);

        // Nút chỉnh sửa thông tin: chuyển sang EditAccountActivity (bạn có thể tự xây dựng Activity này)
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
                startActivity(intent);
            }
        });
        btnQuanLyBaiDang.setOnClickListener(v -> {
            // Chuyển sang ManagePostsActivity để quản lý bài đăng của mình
            Intent intent = new Intent(AccountActivity.this, ManagePostsActivity.class);
            startActivity(intent);
        });

        // Nút đăng xuất: xóa SharedPreferences và chuyển về màn hình đăng nhập
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}

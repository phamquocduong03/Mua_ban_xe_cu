package com.example.mua_ban_xe_cu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.API.AppConfig;
import com.example.mua_ban_xe_cu.database.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditAccountActivity extends AppCompatActivity {

    private EditText etName, etUsername, etEmail, etFacebook, etProvince;
    private Button btnSave;
    private ApiService apiService;
    private SharedPreferences preferences;
    private String oldPhoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etFacebook = findViewById(R.id.etFacebook);
        etProvince = findViewById(R.id.etProvince);
        btnSave = findViewById(R.id.btnSave);

        preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = preferences.getString("user_name", "");
        String username = preferences.getString("user_username", "");
        String email = preferences.getString("user_email", "");
        String province = preferences.getString("user_province", "");
        String facebookLink = preferences.getString("facebookLink", "");
        oldPhoneNumber = preferences.getString("phoneNumber", "");

        etName.setText(name);
        etUsername.setText(username);
        etEmail.setText(email);
        etFacebook.setText(facebookLink);
        etProvince.setText(province);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newUsername = etUsername.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String facebook = etFacebook.getText().toString().trim();
            String provinceText = etProvince.getText().toString().trim();

            if (newName.isEmpty() || newUsername.isEmpty() || newEmail.isEmpty()
                    || provinceText.isEmpty() || facebook.isEmpty() || oldPhoneNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            User updatedUser = new User(newName, newUsername, "", newEmail, provinceText, facebook, "", oldPhoneNumber, false);

            apiService.updateUser(updatedUser).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_name", newName);
                        editor.putString("user_username", newUsername);
                        editor.putString("user_email", newEmail);
                        editor.putString("user_province", provinceText);
                        editor.putString("facebookLink", facebook);
                        editor.apply();

                        Toast.makeText(EditAccountActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditAccountActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Đóng EditAccountActivity
                    } else {
                        Toast.makeText(EditAccountActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(EditAccountActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

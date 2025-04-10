package com.example.mua_ban_xe_cu.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mua_ban_xe_cu.API.ApiClient;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.Car;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCarActivity extends AppCompatActivity {

    private EditText editName, editPrice, editYear, editPhone, editProvince;
    private Spinner spinnerBrand, spinnerType;
    private Button btnSave;

    private String carId, imageUrl, createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        // Khởi tạo view
        editName = findViewById(R.id.editCarName);
        editPrice = findViewById(R.id.editCarPrice);
        editYear = findViewById(R.id.editCarYear);
        editPhone = findViewById(R.id.editPhoneNumber);
        editProvince = findViewById(R.id.editProvince);
        spinnerBrand = findViewById(R.id.spinnerCarBrand);
        spinnerType = findViewById(R.id.spinnerCarType);
        btnSave = findViewById(R.id.btnSaveEdit);

        // Thiết lập dữ liệu spinner
        String[] brands = {"Honda", "Yamaha", "Suzuki", "Vinfast", "Khác"};
        spinnerBrand.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brands));
        String[] types = {"Xe số", "Xe tay ga", "Xe côn tay", "Xe điện"};
        spinnerType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));

        // Lấy dữ liệu từ Intent
        carId = getIntent().getStringExtra("carId");
        imageUrl = getIntent().getStringExtra("imageUrl");
        createdBy = getIntent().getStringExtra("createdBy");

        editName.setText(getIntent().getStringExtra("name"));
        editPrice.setText(getIntent().getStringExtra("price"));
        editYear.setText(getIntent().getStringExtra("year"));
        editPhone.setText(getIntent().getStringExtra("phoneNumber"));
        editProvince.setText(getIntent().getStringExtra("province"));

        // Set giá trị Spinner
        String brand = getIntent().getStringExtra("brand");
        if (brand != null) spinnerBrand.setSelection(((ArrayAdapter) spinnerBrand.getAdapter()).getPosition(brand));

        String type = getIntent().getStringExtra("type");
        if (type != null) spinnerType.setSelection(((ArrayAdapter) spinnerType.getAdapter()).getPosition(type));

        // Xử lý khi nhấn lưu
        btnSave.setOnClickListener(v -> {
            Car updatedCar = new Car();
            updatedCar.setId(carId);
            updatedCar.setName(editName.getText().toString());
            updatedCar.setBrand(spinnerBrand.getSelectedItem().toString());
            updatedCar.setYear(editYear.getText().toString());
            updatedCar.setPrice(editPrice.getText().toString());
            updatedCar.setPhoneNumber(editPhone.getText().toString());
            updatedCar.setProvince(editProvince.getText().toString());
            updatedCar.setImageUrl(imageUrl);
            updatedCar.setCreatedBy(createdBy);
            updatedCar.setType(spinnerType.getSelectedItem().toString());

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.updateCar(updatedCar).enqueue(new Callback<Car>() {
                @Override
                public void onResponse(Call<Car> call, Response<Car> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditCarActivity.this, "Cập nhật bài đăng thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditCarActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Car> call, Throwable t) {
                    Toast.makeText(EditCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

package com.example.mua_ban_xe_cu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mua_ban_xe_cu.Model.Response.PostCarResponse;
import com.example.mua_ban_xe_cu.Model.Response.UploadResponse;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.API.ApiClient;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.database.Car;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCarActivity extends AppCompatActivity {

    private EditText editCarName, editCarYear, editCarPrice;
    private Spinner spinnerCarBrand, spinnerCarType;
    private Button btnUploadImage, btnPost;
    private Uri selectedImageUri;

    private ApiService apiService;
    private static final int REQUEST_CODE_READ_MEDIA_IMAGES = 1;

    private ActivityResultLauncher<Intent> photoPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        // Cập nhật lại base URL trước khi sử dụng ApiClient
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize UI components
        editCarName = findViewById(R.id.editCarName);
        editCarYear = findViewById(R.id.editCarYear);
        editCarPrice = findViewById(R.id.editCarPrice);
        spinnerCarBrand = findViewById(R.id.spinnerCarBrand);
        spinnerCarType = findViewById(R.id.spinnerCarType);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnPost = findViewById(R.id.btnPost);

        // Cung cấp dữ liệu cho Spinner (nhãn hiệu xe)
        String[] carBrands = {"Honda", "Yamaha", "Suzuki", "Vinfast", "Khác"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carBrands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarBrand.setAdapter(brandAdapter);

        // Cung cấp dữ liệu cho Spinner (loại xe)
        String[] carTypes = {"Xe số", "Xe tay ga", "Xe côn tay", "Xe điện"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarType.setAdapter(typeAdapter);

        // Handle the Photo Picker for Android 14 and later
        photoPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                selectedImageUri = result.getData().getData();
            }
        });

        // Kiểm tra quyền khi người dùng bấm chọn ảnh
        btnUploadImage.setOnClickListener(v -> openGallery());

        // Xử lý sự kiện khi người dùng bấm Đăng bài
        btnPost.setOnClickListener(v -> {
            String carName = editCarName.getText().toString();
            String carYear = editCarYear.getText().toString();
            String carPrice = editCarPrice.getText().toString();

            if (selectedImageUri != null && !TextUtils.isEmpty(carName)
                    && !TextUtils.isEmpty(carYear) && !TextUtils.isEmpty(carPrice)) {
                // Lấy dữ liệu từ Spinner
                String selectedBrand = spinnerCarBrand.getSelectedItem().toString();
                String selectedType = spinnerCarType.getSelectedItem().toString();

                // Tiến hành tải ảnh lên server và lưu thông tin bài đăng
                uploadImageToServer(selectedImageUri, carName, carYear, carPrice, selectedBrand, selectedType);
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Mở thư viện ảnh cho các phiên bản Android cũ hơn và kiểm tra quyền
    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_READ_MEDIA_IMAGES);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerLauncher.launch(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_MEDIA_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Quyền truy cập ảnh bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm để tải ảnh lên server
    private void uploadImageToServer(Uri imageUri, String carName, String carYear, String carPrice, String brand, String type) {
        try {
            Log.d("UploadImage", "Bắt đầu tải ảnh lên server");

            String filePath = getRealPathFromURI(imageUri);
            if (filePath == null) {
                Log.e("UploadImage", "Không thể lấy đường dẫn tệp từ URI");
                return;
            }

            File file = new File(filePath);
            Log.d("UploadImage", "Đã lấy file từ URI: " + file.getAbsolutePath());

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

            apiService.uploadImage(part).enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (response.isSuccessful()) {
                        String imageUrl = response.body().getImageUrl();
                        imageUrl = imageUrl.replace("http://car-service:5002", "http://localhost:5002");
                        Log.d("UploadImage", "Tải ảnh thành công. URL ảnh: " + imageUrl);


// Cập nhật lại URL ảnh vào đối tượng xe
                        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        String userEmail = preferences.getString("user_email", "user@example.com");
                        String phoneNumber = preferences.getString("phoneNumber", "Không xác định");
                        String userProvince = preferences.getString("user_province", "Không xác định");

                        // Chú ý: Constructor của Car yêu cầu 10 tham số (id, carName, brand, carYear, carPrice, userProvince, imageUrl, userEmail, phoneNumber, type)
                        // Nếu id chưa có, ta truyền chuỗi rỗng.
                        // Cập nhật lại URL ảnh vào đối tượng xe
                        Car car = new Car("", carName, brand, carYear, carPrice, userProvince, imageUrl, userEmail, phoneNumber, type);
                        postCarToServer(car);
                        Log.d("PostCar", "Dữ liệu gửi lên server: " + car.toString());
                    } else {
                        Log.e("UploadImage", "Upload ảnh thất bại. Mã lỗi: " + response.code());
                        Toast.makeText(PostCarActivity.this, "Upload ảnh thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Log.e("UploadImage", "Lỗi khi gửi ảnh lên server: " + t.getMessage());
                    Toast.makeText(PostCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("UploadImage", "Lỗi không xác định: " + e.getMessage());
            Toast.makeText(PostCarActivity.this, "Đã xảy ra lỗi khi tải ảnh lên.", Toast.LENGTH_SHORT).show();
        }
    }

    private void postCarToServer(Car car) {
        apiService.postCar(car).enqueue(new Callback<PostCarResponse>() {
            @Override
            public void onResponse(Call<PostCarResponse> call, Response<PostCarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostCarResponse resp = response.body();
                    // Giả sử server trả về carId trong resp.carId
                    String newCarId = resp.getCarId(); // Bạn cần đảm bảo lớp PostCarResponse có getter cho carId
                    Log.d("PostCar", "Bài đăng thành công: " + resp.toString());
                    Log.d("PostCar", "Dữ liệu gửi lên server: " + car.toString());
                    // Nếu bạn muốn cập nhật đối tượng Car với id mới từ server:
                    car.setId(newCarId);
                    Toast.makeText(PostCarActivity.this, "Bài đăng thành công! Car ID: " + newCarId, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("PostCar", "Đăng bài thất bại: " + response.code());
                    Toast.makeText(PostCarActivity.this, "Đăng bài thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostCarResponse> call, Throwable t) {
                Toast.makeText(PostCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm lấy đường dẫn thực tế từ URI
    private String getRealPathFromURI(Uri uri) {
        String path = null;
        if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        }
        return path;
    }
}

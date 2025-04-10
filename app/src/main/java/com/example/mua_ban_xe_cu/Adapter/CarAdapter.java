package com.example.mua_ban_xe_cu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mua_ban_xe_cu.Activity.CarDetailActivity;
import com.example.mua_ban_xe_cu.database.Car;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.API.AppConfig;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> carList;
    private boolean isOwner;

    public CarAdapter(List<Car> carList, boolean isOwner) {
        this.carList = carList;
        this.isOwner = isOwner;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = carList.get(position);

        holder.carName.setText(car.getName());
        holder.carBrand.setText("Hãng xe: " + car.getBrand());
        holder.carYear.setText("Năm sản xuất: " + car.getYear());
        String formattedPrice = formatPrice(car.getPrice());
        holder.carPrice.setText("Giá: " + formattedPrice);
        holder.carProvince.setText("Địa chỉ: " + (car.getProvince() != null ? car.getProvince() : "Không xác định"));

        // Khi nhấn vào item, chuyển sang CarDetailActivity và truyền dữ liệu, bao gồm posterName
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, CarDetailActivity.class);
            intent.putExtra("name", car.getName());
            intent.putExtra("price", car.getPrice());
            intent.putExtra("imageUrl", car.getImageUrl());
            intent.putExtra("posterName", car.getPosterName()); // Truyền tên người đăng
            intent.putExtra("address", car.getProvince());
            intent.putExtra("phoneNumber", car.getPhoneNumber());
            intent.putExtra("email", car.getCreatedBy());
            intent.putExtra("carId", car.getId());
            intent.putExtra("isOwner", isOwner);
            context.startActivity(intent);
        });

        String imageUrl = car.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String updatedImageUrl = imageUrl.replace("http://localhost:5002/uploads/", AppConfig.getBaseUrl() + "car/uploads/");
            Glide.with(holder.itemView.getContext()).load(updatedImageUrl).into(holder.imageCar);
        } else {
            Glide.with(holder.itemView.getContext()).load(R.drawable.ic_car_placeholder).into(holder.imageCar);
        }
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public TextView carName, carBrand, carYear, carPrice, carProvince;
        public ImageView imageCar;

        public CarViewHolder(View view) {
            super(view);
            carName = view.findViewById(R.id.textCarName);
            carBrand = view.findViewById(R.id.textCarBrand);
            carYear = view.findViewById(R.id.textCarYear);
            carPrice = view.findViewById(R.id.textCarPrice);
            carProvince = view.findViewById(R.id.textCarProvince);
            imageCar = view.findViewById(R.id.imageCar);
        }
    }

    private String formatPrice(String price) {
        try {
            long priceValue = Long.parseLong(price);
            java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();
            return numberFormat.format(priceValue);
        } catch (NumberFormatException e) {
            return price;
        }
    }
}

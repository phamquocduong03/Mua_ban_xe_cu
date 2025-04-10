package com.example.mua_ban_xe_cu.database;

import com.google.gson.annotations.SerializedName;

public class Car {
    @SerializedName("_id") // Ánh xạ trường _id từ JSON sang thuộc tính id
    private String id;
    private String phoneNumber;
    private String name;
    private String brand;
    private String year;
    private String price;
    private String province;
    private String imageUrl;
    private String createdBy;
    private String status;
    private String type;

    // ✅ Constructor mặc định (rỗng) – cần cho Gson, Retrofit, và khi new Car()
    public Car() {
    }

    // ✅ Constructor đầy đủ (nếu muốn tạo Car có đủ thông tin)
    public Car(String id, String name, String brand, String year, String price,
               String province, String imageUrl, String createdBy,
               String phoneNumber, String type) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.province = province;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    // ✅ Getter và Setter đầy đủ
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}

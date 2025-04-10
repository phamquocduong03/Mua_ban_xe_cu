package com.example.mua_ban_xe_cu.database;

import com.google.gson.annotations.SerializedName;

public class User {

    // Lưu _id từ MongoDB (trường này có thể nhận giá trị từ API)
    @SerializedName("userId")
    private String id;


    @SerializedName("name")
    private String name; // Thêm trường name (họ và tên)

    private String username;
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("facebookLink")
    private String facebookLink;

    @SerializedName("dob")
    private String dob;

    @SerializedName("isAdmin")
    private boolean isAdmin;

    @SerializedName("province")
    private String province;

    @SerializedName("token")
    private String token;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    // Constructor dùng cho đăng nhập (với 9 tham số)
    public User(String name, String username, String password, String email, String province, String facebookLink, String dob, String phoneNumber, boolean isAdmin) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.province = province;
        this.facebookLink = facebookLink;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }

    // Getter và setter cho id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Các getters và setters khác
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFacebookLink() {
        return facebookLink;
    }
    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

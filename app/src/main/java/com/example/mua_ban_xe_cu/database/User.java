package com.example.mua_ban_xe_cu.database;

import com.google.gson.annotations.SerializedName;

public class User {

    private String username;
    private String password;

    @SerializedName("name")
    private String name;
    @SerializedName("email") // Ánh xạ với trường "email" trong JSON
    private String email;

    @SerializedName("facebookLink") // Ánh xạ với trường "facebookLink" trong JSON
    private String facebookLink;

    @SerializedName("dob") // Ánh xạ với trường "dob" trong JSON
    private String dob;

    @SerializedName("isAdmin") // Ánh xạ với trường "isAdmin" trong JSON
    private boolean isAdmin;

    @SerializedName("province") // Ánh xạ với trường "province" trong JSON
    private String province;

    @SerializedName("token") // Ánh xạ với trường "token" trong JSON
    private String token;

    @SerializedName("phoneNumber") // Ánh xạ với trường "phoneNumber" trong JSON
    private String phoneNumber;

    // Constructor có email
    public User(String username, String password, String email, String province, String facebookLink, String dob,String phoneNumber, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.facebookLink = facebookLink;
        this.dob = dob;
        this.isAdmin = isAdmin;
        this.province = province;
        this.phoneNumber = phoneNumber;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Constructor không có email (dùng khi không cần email ngay lập tức)
    public User(String username, String password, String province) {
        this.username = username;
        this.password = password;
        this.province = province;
    }

    // Getter và Setter cho tất cả các trường
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    // Getter và Setter cho phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    // ToString để log dễ dàng (nếu cần)
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", facebookLink='" + facebookLink + '\'' +
                ", dob='" + dob + '\'' +
                ", isAdmin=" + isAdmin +
                ", province='" + province + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

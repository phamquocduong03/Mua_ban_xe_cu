package com.example.mua_ban_xe_cu.Model;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    // Ghi đè phương thức toString để trả về tên tỉnh thành
    @Override
    public String toString() {
        return name; // Trả về tên tỉnh thành khi Spinner hiển thị
    }
}

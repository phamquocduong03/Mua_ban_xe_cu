package com.example.mua_ban_xe_cu.Model;

import com.google.gson.annotations.SerializedName;

public class Ward {
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
}
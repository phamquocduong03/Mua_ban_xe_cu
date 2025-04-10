package com.example.mua_ban_xe_cu.Model.Response;

public class PostCarResponse {
    private String message;
    private String carId; // Thêm trường này

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return "PostCarResponse{" +
                "message='" + message + '\'' +
                ", carId='" + carId + '\'' +
                '}';
    }
}

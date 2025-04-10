package com.example.mua_ban_xe_cu.database;

import java.util.Date;

public class Message {
    private String sender;
    private String receiver;
    private String carId;
    private String content;
    private Date timestamp;  // Thêm trường timestamp

    // Constructor đầy đủ
    public Message(String sender, String receiver, String carId, String content, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.carId = carId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Constructor không có timestamp nếu cần
    public Message(String sender, String receiver, String carId, String content) {
        this(sender, receiver, carId, content, new Date());
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getCarId() {
        return carId;
    }
    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

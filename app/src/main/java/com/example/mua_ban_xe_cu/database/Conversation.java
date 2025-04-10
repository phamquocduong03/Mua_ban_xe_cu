package com.example.mua_ban_xe_cu.database;

import java.util.Date;

public class Conversation {
    private String otherParty;
    private String carId;
    private String lastMessage;
    private Date timestamp;

    public Conversation(String otherParty, String carId, String lastMessage, Date timestamp) {
        this.otherParty = otherParty;
        this.carId = carId;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getOtherParty() {
        return otherParty;
    }

    public void setOtherParty(String otherParty) {
        this.otherParty = otherParty;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

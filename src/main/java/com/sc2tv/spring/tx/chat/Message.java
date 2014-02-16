package com.sc2tv.spring.tx.chat;

public class Message {
    private String id;
    private String uid;
    private String message;
    private String date;
    private String channelId;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Message() {
    }

    public Message(String id, String uid, String message, String date, String channelId, String role, String name) {
        this.id = id;
        this.uid = uid;
        this.message = message;
        this.date = date;
        this.channelId = channelId;
        this.role = role;
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

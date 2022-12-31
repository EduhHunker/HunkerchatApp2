package com.example.hunkerchat_app;

public class firebasemodel {
    static String name;
    static String uid;
    static String status;
    static String image;

    public firebasemodel(String name, String uid, String status, String image) {
        this.name = name;
        this.uid = uid;
        this.status = status;
        this.image = image;

    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

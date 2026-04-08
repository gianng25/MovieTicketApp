package com.example.movieticketfirebase.model;

public class UserProfile {
    private String uid;
    private String name;
    private String email;
    private String phone;

    public UserProfile() {}

    public UserProfile(String uid, String name, String email, String phone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getUid() { return uid; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}

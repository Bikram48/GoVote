package com.example.govote.Model;

public class User {
    private String email;
    private String password;
    private String phoneNumber;
    private String userRole;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String password, String phoneNumber, String userRole) {
        this.email = email;
        this.password = password;
        this.phoneNumber=phoneNumber;
        this.userRole=userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

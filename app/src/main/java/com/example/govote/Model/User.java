package com.example.govote.Model;

public class User {
    private String email;
    private String password;
    private String userRole;

    public User(String email, String password,String userRole) {
        this.email = email;
        this.password = password;
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

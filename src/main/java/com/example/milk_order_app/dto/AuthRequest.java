package com.example.milk_order_app.dto;

public class AuthRequest {

    // 🔥 username OR email OR mobile
    private String loginId;

    private String password;

    public AuthRequest() {}

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
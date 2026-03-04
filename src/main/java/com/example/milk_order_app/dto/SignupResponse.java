package com.example.milk_order_app.dto;

public class SignupResponse {

    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String role;

    public SignupResponse(Long id,
                          String username,
                          String email,
                          String mobile,
                          String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getRole() { return role; }
}


package com.example.milk_order_app.dto;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String role;

    private String address;

    public UserResponse(Long id, String username,
                        String email, String mobile,
                        String role,String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
        this.address = address;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getRole() { return role; }

    public String getAddress() { return address; }
}


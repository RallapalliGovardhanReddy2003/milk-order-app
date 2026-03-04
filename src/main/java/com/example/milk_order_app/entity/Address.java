package com.example.milk_order_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Address {

    @Column(name = "door_no")
    private String doorNo;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "street")
    private String street;

    @Column(name = "pincode")
    private String pincode;

    public Address() {}

    public Address(String doorNo,
                   String landmark,
                   String street,
                   String pincode) {
        this.doorNo = doorNo;
        this.landmark = landmark;
        this.street = street;
        this.pincode = pincode;
    }

    // ======================
    // GETTERS & SETTERS
    // ======================

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
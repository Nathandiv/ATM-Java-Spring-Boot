package com.example.atm_app.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private int pin;

    public LoginRequest() {} // required for JSON mapping
}
package com.example.atm_app.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private int pin;
    private float initialBalance;
}
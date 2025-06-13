package com.example.atm_app.dto;


import lombok.Data;

@Data
public class WithdrawRequest {

    private int accountId;
    private float amount;

}

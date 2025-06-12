package com.example.atm_app.dto;

import lombok.Data;

@Data
public class AddBeneficiaryRequest {
    private int senderId;
    private int recipientId;
}

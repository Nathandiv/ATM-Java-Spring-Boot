package com.example.atm_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryId implements Serializable {
    private Integer senderId;
    private Integer recipientId;
}
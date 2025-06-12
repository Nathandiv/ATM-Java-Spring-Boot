package com.example.atm_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BeneficiaryId.class)
public class Beneficiary {

    @Id
    @Column(name = "sender_id", nullable = false, updatable = false)
    private Integer senderId;

    @Id
    @Column(name = "recipient_id", nullable = false, updatable = false)
    private Integer recipientId;
}
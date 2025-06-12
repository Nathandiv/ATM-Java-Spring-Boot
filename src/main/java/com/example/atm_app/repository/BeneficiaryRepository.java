package com.example.atm_app.repository;

import com.example.atm_app.entity.Beneficiary;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, BeneficiaryId> {
    List<Beneficiary> findBySenderId(int senderId);
    List<Beneficiary> findByRecipientId(int recipientId);
}

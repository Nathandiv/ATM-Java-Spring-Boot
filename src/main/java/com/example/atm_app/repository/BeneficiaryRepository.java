package com.example.atm_app.repository;

import com.example.atm_app.entity.Beneficiary;
import com.example.atm_app.entity.BeneficiaryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, BeneficiaryId> {
    List<Beneficiary> findBySenderId(int senderId);
    List<Beneficiary> findByRecipientId(int recipientId);
}

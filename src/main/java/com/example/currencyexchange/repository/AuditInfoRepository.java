package com.example.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.currencyexchange.models.AuditInfo;

public interface AuditInfoRepository extends JpaRepository<AuditInfo, Long>{
    
}
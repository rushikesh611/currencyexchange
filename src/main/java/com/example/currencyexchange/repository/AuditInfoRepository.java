package com.example.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.currencyexchange.models.AuditInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditInfoRepository extends JpaRepository<AuditInfo, Long>{
    
}
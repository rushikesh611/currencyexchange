package com.example.currencyexchange.repository;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.currencyexchange.models.AuditInfo;

@Repository
public interface AuditInfoRepository extends JpaRepository<AuditInfo, Integer>{
    
//    @Query(value = "select * from audit_info where request = ?0", nativeQuery = true)
    List<AuditInfo> findByRequest(String request);
} 
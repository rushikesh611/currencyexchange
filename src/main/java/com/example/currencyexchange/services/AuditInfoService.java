package com.example.currencyexchange.services;

import java.util.List;

import com.example.currencyexchange.models.AuditInfo;

public interface AuditInfoService {
    AuditInfo createLog(AuditInfo auditInfo);

    AuditInfo updateLog(AuditInfo auditInfo);

    List<AuditInfo> getAllLogs();

    AuditInfo getLogById(Integer requestId);

    void deleteLog(Integer requestId);

}

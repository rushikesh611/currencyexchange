package com.example.currencyexchange.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.currencyexchange.models.AuditInfo;
import com.example.currencyexchange.repository.AuditInfoRepository;

@Service
public class AuditInfoServiceImpl implements AuditInfoService{

    @Autowired
    private AuditInfoRepository auditInfoRepository;

    @Override
    public AuditInfo createLog(AuditInfo auditInfo) {
        return auditInfoRepository.save(auditInfo);
    }

    @Override
    public AuditInfo updateLog(AuditInfo auditInfo) {
        Optional<AuditInfo> auditObj = this.auditInfoRepository.findById(auditInfo.getRequestId());

        if(auditObj.isPresent()){
            AuditInfo auditUpdate = auditObj.get();
            auditUpdate.setRequest(auditInfo.getRequest());
            auditUpdate.setResponse(auditInfo.getResponse());
            auditUpdate.setStatus(auditInfo.getStatus());
            return this.auditInfoRepository.save(auditUpdate);
        } else {
            throw new RuntimeException("Audit not found with id " + auditInfo.getRequestId());
        }
    }

    @Override
    public List<AuditInfo> getAllLogs() {
        return this.auditInfoRepository.findAll();
    }

    @Override
    public AuditInfo getLogById(Integer requestId) {
        Optional<AuditInfo> auditObj = this.auditInfoRepository.findById(requestId);

        if(auditObj.isPresent()){
            return auditObj.get();
        } else {
            throw new RuntimeException("Audit not found with id " + requestId);
        }
    }

    @Override
    public void deleteLog(Integer requestId) {
        Optional<AuditInfo> auditObj = this.auditInfoRepository.findById(requestId);
        if(auditObj.isPresent()){
            this.auditInfoRepository.deleteById(requestId);
        } else {
            throw new RuntimeException("Audit not found with id " + requestId);
        }
        
    }   
}

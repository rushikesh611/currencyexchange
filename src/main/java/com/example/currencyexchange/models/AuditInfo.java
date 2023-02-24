package com.example.currencyexchange.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "audit_info")
public class AuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "requestId")
    private Integer requestId;

    public enum RequestStatus {
        SENT_REQUEST,
        RECEIVED_RESPONSE
    }
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @CreationTimestamp
    @Column(name = "createdTS", updatable = false)
    private Date createdTimestamp;

    @UpdateTimestamp
    @Column(name = "updatedTS", updatable = true)
    private Date updatedTimestamp;

}

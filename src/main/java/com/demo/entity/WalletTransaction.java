package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wallettransactions")
public class WalletTransaction {

    @Id
    @Column(name = "transactionid",nullable = false,updatable = false)
    private String transactionId;

    @Column(name = "transactiontype", nullable = false)
    private String transactionType;

    private Integer amount;

    @Column(name = "loginid")
    private String loginId;

    @CreationTimestamp
    @Column(name = "transactiontime")
    private Date transactionTime;

    @Column(name = "referenceid",nullable = false,updatable = false)
    private String referenceId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        transactionId = dtf.format(now);

        this.transactionId = "W"+transactionId;
    }
}

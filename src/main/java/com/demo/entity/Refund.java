package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "refunds")
public class Refund {

    @Id
    @Column(name = "transactionid", nullable = false)
    private String transactionId;

    private String product;

    private Integer amount;

    private Date date;

    @Column(name = "loginid", nullable = false)
    private String loginId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        transactionId = dtf.format(now);

        this.transactionId = "REF"+transactionId;
    }
}

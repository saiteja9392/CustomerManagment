package com.demo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

    @Id
    @Column(name = "transactionid")
    private String transactionId;

    @Column(name = "productid")
    private String productId;

    @Column(name = "productname")
    private String productName;

    private Integer quantity;

    private String loginId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        transactionId = "WISH"+dtf.format(now);

        this.transactionId = transactionId;
    }
}

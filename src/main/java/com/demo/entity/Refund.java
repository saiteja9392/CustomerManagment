package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Refund {

    @Id
    @Column(name = "transactionid", nullable = false)
    private String transactionId;

    private String product;

    private Integer amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "loginid", nullable = false)
    private String loginId;
}

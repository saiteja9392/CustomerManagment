package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Details {

    private String productName;
    private Integer quantity;
    private int totalAmount;
    private Date dateOfPurchase;
    private String status;
    private Date dateOfRefund;
}

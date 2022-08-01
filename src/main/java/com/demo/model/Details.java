package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Details {

    private String productName;
    private Integer quantity;
    private int totalAmount;
    private String dateOfPurchase;
}

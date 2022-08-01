package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullInformation {

    private String customerId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private LoginDetails loginDetails;
    private WalletDetails walletDetails;
    private OrderDetails orderDetails;
    private List<RefundDetails> refundDetails;
}

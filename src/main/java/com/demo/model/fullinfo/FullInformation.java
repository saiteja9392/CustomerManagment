package com.demo.model.fullinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String emailAddress;
    private LoginDetails loginDetails;
    private WalletDetails walletDetails;
    private OrderDetails orderDetails;
}

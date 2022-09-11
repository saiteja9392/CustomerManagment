package com.demo.model.order;

import com.demo.entity.order.OrderSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlacedOrder {

    private String transactionId;
    private Date dateOfPurchase;
    private String promoCode;
    private Integer amount;
    private List<OrderSummary> orderList;
}

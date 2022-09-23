package com.demo.entity.order;

import com.demo.entity.Refund;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class OrderSummary {

    @Id
    private String orderSummaryTransactionId;

    @JsonIgnore
    private String username;

    private String product;

    @Column(name = "dateofpurchase")
    private Date dateOfPurchase;

    @NotNull
    private Integer quantity;

    @Column(name = "productprice",nullable = false)
    private Integer productPrice;

    @Column(name = "offeramount")
    private Integer offerAmount;

    @Column(name = "offersapplied")
    private String offersApplied;

    @Column(name = "finalprice",nullable = false)
    private int finalPrice;

    private String status;

    @OneToOne
    @JoinColumn(name = "refund_transactionid")
    private Refund refund;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "order_transaction_id", nullable = false)
    private Order order;

    public synchronized void setOrderSummaryTransactionId(String orderSummaryTransactionId) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDateTime now = LocalDateTime.now();
        orderSummaryTransactionId = "ORD"+dtf.format(now);

        this.orderSummaryTransactionId = orderSummaryTransactionId;
    }

    public String getOrderSummaryTransactionId() {
        return orderSummaryTransactionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(Integer offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(String offersApplied) {
        this.offersApplied = offersApplied;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

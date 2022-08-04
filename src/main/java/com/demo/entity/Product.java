package com.demo.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @Column(name = "productid",nullable = false)
    private String productId;

    @Column(name = "productname", nullable = false)
    private String productName;

    private Integer price;

    private String manufacturer;

    @Column(name = "quantity")
    private Integer quantityInStore;

    @CreatedBy
    @Column(name = "createdby")
    private String createdBy;

    @OneToOne
    @JoinColumn(name = "offerkey")
    private Offer offer;
}

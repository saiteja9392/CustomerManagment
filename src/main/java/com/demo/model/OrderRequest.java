package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    @Size(min = 1, message = "Product Id Is Mandatory")
    private String productId;

    @NotNull
    @Min(value = 1, message = "Please Specify Quantity")
    private Integer quantity;

    private String promoCode;
}

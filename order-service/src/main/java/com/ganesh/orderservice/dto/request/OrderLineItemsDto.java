package com.ganesh.orderservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    private long id;
    private String skuCode;//SKU stands for "Stock Keeping Unit." A SKU is a unique code assigned to each distinct product in a store or inventory. It helps in tracking and managing inventory efficiently.
    private BigDecimal price;
    private Integer quantity;
}

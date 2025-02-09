package com.asifiqbalsekh.EcomBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private ProductDTO product;
    private Integer itemQuantity;
    private double itemDiscount;
    private double itemProductPrice;
}

package com.asifiqbalsekh.EcomBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private Long cartId;
    private Double totalPrice;
    private List<CartItemResponseDTO> cartItems =new ArrayList<>();

}

package com.asifiqbalsekh.EcomBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private PaymentDTO paymentInfo;
    private Double totalAmount;
    private String orderStatus;
    private AddressDTO shippingAddress;
}

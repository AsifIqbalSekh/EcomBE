package com.asifiqbalsekh.EcomBE.service;

import com.asifiqbalsekh.EcomBE.dto.OrderRequestDTO;
import com.asifiqbalsekh.EcomBE.dto.OrderResponseDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    @Transactional
    OrderResponseDTO placeOrder(@Valid OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> getOrderHistoryOfUser();

}

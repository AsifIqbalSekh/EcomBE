package com.asifiqbalsekh.EcomBE.controller;

import com.asifiqbalsekh.EcomBE.dto.OrderRequestDTO;
import com.asifiqbalsekh.EcomBE.dto.OrderResponseDTO;
import com.asifiqbalsekh.EcomBE.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/place-order")
    public ResponseEntity<OrderResponseDTO> orderProducts(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {

        OrderResponseDTO orderResponseDTO= orderService.placeOrder(orderRequestDTO);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }
    @GetMapping("/order/order-history/user")
    public ResponseEntity<List<OrderResponseDTO>> getOrderHistory() {
        return new ResponseEntity<>(orderService.getOrderHistoryOfUser(),HttpStatus.FOUND);
    }
}

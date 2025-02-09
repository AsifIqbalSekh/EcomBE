package com.asifiqbalsekh.EcomBE.repository;

import com.asifiqbalsekh.EcomBE.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

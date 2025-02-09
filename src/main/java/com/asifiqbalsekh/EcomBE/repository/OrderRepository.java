package com.asifiqbalsekh.EcomBE.repository;

import com.asifiqbalsekh.EcomBE.model.Orders;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByEmail(@Email String email);

    @Query("SELECT o FROM Orders o JOIN FETCH o.orderItems oi JOIN FETCH oi.product p WHERE p.productId = ?1")
    List<Orders> findAllOrdersByProductId(Long productId);
}

package com.asifiqbalsekh.EcomBE.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column(nullable = false)
    private String email;

    private LocalDate orderDate;
    private Double totalAmount;
    private String orderStatus;

    @OneToMany(mappedBy = "orders",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<OrderItem>orderItems=new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address shippingAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

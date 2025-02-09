package com.asifiqbalsekh.EcomBE.service.implementation;

import com.asifiqbalsekh.EcomBE.config.AuthUtil;
import com.asifiqbalsekh.EcomBE.dto.OrderItemDTO;
import com.asifiqbalsekh.EcomBE.dto.OrderRequestDTO;
import com.asifiqbalsekh.EcomBE.dto.OrderResponseDTO;
import com.asifiqbalsekh.EcomBE.dto.PaymentDTO;
import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.*;
import com.asifiqbalsekh.EcomBE.repository.*;
import com.asifiqbalsekh.EcomBE.service.CartService;
import com.asifiqbalsekh.EcomBE.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AuthUtil authUtil;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        User user=authUtil.userDetails();
        log.info("user details fetch for placeOrder");

        Cart cart=cartRepository.findCartByEmail(user.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Cart","email", user.getEmail())
        );
        List<CartItem>cartItems=cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        log.info("user cart fetch for placeOrder");

        Address shippingAddress = user.getAddresses().stream()
                .filter(item -> item.getAddressId().equals(orderRequestDTO.getAddressId()))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId", orderRequestDTO.getAddressId()));

        log.info("user address fetch for placeOrder");

        Orders orders = new Orders();
        orders.setEmail(user.getEmail());
        orders.setOrderDate(LocalDate.now());
        orders.setShippingAddress(shippingAddress);
        orders.setTotalAmount(cart.getTotalPrice());
        orders.setOrderStatus("APPROVED");
        orders.setUser(user);

        Payment payment;
        if(orderRequestDTO.getPaymentMethod().equalsIgnoreCase("OFFLINE")){
            payment=new Payment("OFFLINE",
                    "NA",
                    "NA",
                    "NA",
                    "NA");
        }
        else{
            payment=new Payment("ONLINE",
                    orderRequestDTO.getPgPaymentId(),
                    orderRequestDTO.getPgStatus(),
                    orderRequestDTO.getPgResponseMessage()
                    ,orderRequestDTO.getPgName());
        }
        payment.setOrders(orders);
        payment=paymentRepository.save(payment);
        log.info("user payment details saved for placeOrder");
        orders.setPayment(payment);
        orders =orderRepository.save(orders);
        log.info("user orders details saved for placeOrder");

        List<OrderItem>orderItems=new ArrayList<>();

        for(CartItem cartItem:cartItems){

            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setItemQuantity(cartItem.getQuantity());
            orderItem.setItemDiscount(cartItem.getDiscount());
            orderItem.setItemProductPrice(cartItem.getProductPrice());
            orderItem.setOrders(orders);
            orderItems.add(orderItem);
        }
        orderItems=orderItemRepository.saveAll(orderItems);
        log.info("user orderItem details saved for placeOrder");

        cart.getCartItems().forEach(cartItem->{
            int cartItemQuantity=cartItem.getQuantity();
            Product product=cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItemQuantity);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(),cartItem.getProduct().getProductId());

        });
        log.info("Product Quantity Reduce & Remove from cart for placeOrder");

        OrderResponseDTO orderResponseDTO = modelMapper.map(orders, OrderResponseDTO.class);
        orderItems.forEach(orderItem->{
            orderResponseDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class));
        });
        orderResponseDTO.setPaymentInfo(modelMapper.map(payment, PaymentDTO.class));

        log.info("Orders Converted to OrderResponseDTO");
        return orderResponseDTO;
    }

    @Override
    public List<OrderResponseDTO> getOrderHistoryOfUser() {
        User user=authUtil.userDetails();
        List<Orders>myOrder=user.getOrdersHistory();
        if(myOrder.isEmpty()){
            throw new ResourceNotFoundException("Order","email", user.getEmail());
        }
        List<OrderResponseDTO> orderResponseDTOS=new ArrayList<>();

        myOrder.forEach(order->{
            OrderResponseDTO orderResponseDTO=modelMapper.map(order, OrderResponseDTO.class);
//            order.getOrderItems().forEach(orderItem->{
//                orderResponseDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class));
//            });
            orderResponseDTO.setPaymentInfo(modelMapper.map(order.getPayment(), PaymentDTO.class));
            orderResponseDTOS.add(orderResponseDTO);
        });

        return orderResponseDTOS;
    }

}

package com.asifiqbalsekh.EcomBE.controller;

import com.asifiqbalsekh.EcomBE.dto.CartResponseDTO;
import com.asifiqbalsekh.EcomBE.model.Cart;
import com.asifiqbalsekh.EcomBE.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartResponseDTO>addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        return new ResponseEntity<>(cartService.addProduct(productId, quantity), HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponseDTO>>getAllCarts(){
        return new ResponseEntity<>(cartService.getCarts(), HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartResponseDTO> getUserCart(){
        return new ResponseEntity<>(cartService.getUserCart(),HttpStatus.FOUND);
    }

    @PutMapping("/carts/products/{productId}/operation/{operation}")
    public ResponseEntity<CartResponseDTO> updateQuantityOfCartItem(@PathVariable Long productId, @PathVariable String operation){
        CartResponseDTO cartResponseDTO = cartService.updateCartItemQuantity(productId,
                operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<>(cartResponseDTO, HttpStatus.OK);

    }
    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<CartResponseDTO> deleteCartItem(@PathVariable Long cartId, @PathVariable Long productId){
        return new ResponseEntity<>(cartService.deleteProductFromCart(cartId, productId), HttpStatus.OK);
    }
}

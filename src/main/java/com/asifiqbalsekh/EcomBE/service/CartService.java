package com.asifiqbalsekh.EcomBE.service;

import com.asifiqbalsekh.EcomBE.dto.CartResponseDTO;
import com.asifiqbalsekh.EcomBE.dto.ProductDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartResponseDTO addProduct(Long productId, Integer quantity);

    List<CartResponseDTO> getCarts();

    CartResponseDTO getUserCart();

    @Transactional
    CartResponseDTO updateCartItemQuantity(Long productId, Integer quantity);

    @Transactional
    CartResponseDTO deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCart(ProductDTO savedProductDTO);

}

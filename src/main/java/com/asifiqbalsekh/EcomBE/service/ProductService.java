package com.asifiqbalsekh.EcomBE.service;

import com.asifiqbalsekh.EcomBE.dto.ProductDTO;
import com.asifiqbalsekh.EcomBE.dto.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getAllProductsUsingCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getSearchProductsUsingKeyword(String keyWord, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    public ProductDTO updateProduct(Long productId, ProductDTO givenProduct);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image);
}

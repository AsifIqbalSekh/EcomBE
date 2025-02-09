package com.asifiqbalsekh.EcomBE.repository;

import com.asifiqbalsekh.EcomBE.model.Category;
import com.asifiqbalsekh.EcomBE.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageDetails);
    Page<Product> findByProductNameContainingIgnoreCase(String keyWord, Pageable pageDetails);
    Product findByProductNameIgnoreCase(String productName);
}

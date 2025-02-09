package com.asifiqbalsekh.EcomBE.controller;

import com.asifiqbalsekh.EcomBE.config.AppConstant;
import com.asifiqbalsekh.EcomBE.dto.ProductDTO;
import com.asifiqbalsekh.EcomBE.dto.ProductResponseDTO;
import com.asifiqbalsekh.EcomBE.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){

        ProductDTO savedProductDTO=productService.addProduct(categoryId,productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);

    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){

        ProductDTO updatedProductDTO=productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);

    }

    @PutMapping("/admin/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@RequestParam MultipartFile image, @PathVariable Long productId){

        ProductDTO updatedProductDTO=productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);

    }


    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){

        ProductDTO productDTO=productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }


    @GetMapping("/public/product")
    public ResponseEntity<ProductResponseDTO>getAllProducts(
            @RequestParam(defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstant.PAGE_VALUE) Integer pageSize,
            @RequestParam(defaultValue = AppConstant.PRODUCT_SORTBY) String sortBy,
            @RequestParam(defaultValue = AppConstant.PRODUCT_SORTORDER) String sortOrder
    ){
        return ResponseEntity.ok( productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder));

    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponseDTO>getAllProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstant.PAGE_VALUE) Integer pageSize,
            @RequestParam(defaultValue = AppConstant.PRODUCT_SORTBY) String sortBy,
            @RequestParam(defaultValue = AppConstant.PRODUCT_SORTORDER) String sortOrder
    ){
        return ResponseEntity.ok( productService.getAllProductsUsingCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder));

    }

    @GetMapping("/public/product/search/{keyWord}")
    public ResponseEntity<ProductResponseDTO>getAllProductsByKeyword(
            @PathVariable String keyWord,
            @RequestParam(defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstant.PAGE_VALUE) Integer pageSize,
            @RequestParam(defaultValue = AppConstant.PRODUCT_SORTBY) String sortBy,
            @RequestParam(defaultValue = AppConstant.PRODUCT_SORTORDER) String sortOrder
    ){
        return new ResponseEntity<>( productService.getSearchProductsUsingKeyword(keyWord,pageNumber,pageSize,sortBy,sortOrder)
        , HttpStatus.FOUND);

    }
}

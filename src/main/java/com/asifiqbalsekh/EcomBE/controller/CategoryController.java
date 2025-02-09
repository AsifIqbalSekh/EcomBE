package com.asifiqbalsekh.EcomBE.controller;


import com.asifiqbalsekh.EcomBE.config.AppConstant;
import com.asifiqbalsekh.EcomBE.dto.CategoryDTO;
import com.asifiqbalsekh.EcomBE.dto.CategoryResponseDTO;
import com.asifiqbalsekh.EcomBE.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;



    @GetMapping("/public/category")
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstant.PAGE_VALUE) Integer pageSize,
            @RequestParam(defaultValue = AppConstant.CATEGORY_SORTBY) String sortBy,
            @RequestParam(defaultValue = AppConstant.CATEGORY_SORTORDER) String sortOrder
    ){
        CategoryResponseDTO categories = categoryService.getCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDto){

        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id){
        CategoryDTO result = categoryService.deleteCategory(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @PutMapping("/admin/category/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category, @PathVariable Long id){
        CategoryDTO result = categoryService.updateCategory(category,id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}

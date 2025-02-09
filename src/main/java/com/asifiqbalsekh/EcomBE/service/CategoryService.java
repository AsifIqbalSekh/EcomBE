package com.asifiqbalsekh.EcomBE.service;

import com.asifiqbalsekh.EcomBE.dto.CategoryDTO;
import com.asifiqbalsekh.EcomBE.dto.CategoryResponseDTO;
import com.asifiqbalsekh.EcomBE.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO getCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO addCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long id);

    CategoryDTO updateCategory(CategoryDTO categoryDto, Long id);
}

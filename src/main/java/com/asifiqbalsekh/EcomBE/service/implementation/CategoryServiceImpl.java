package com.asifiqbalsekh.EcomBE.service.implementation;

import com.asifiqbalsekh.EcomBE.dto.CategoryDTO;
import com.asifiqbalsekh.EcomBE.dto.CategoryResponseDTO;
import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.Category;
import com.asifiqbalsekh.EcomBE.repository.CategoryRepository;
import com.asifiqbalsekh.EcomBE.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public CategoryResponseDTO getCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder){

        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC,sortBy)
                : Sort.by(Sort.Direction.DESC,sortBy);

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        if (categoryPage.getContent().isEmpty()) {
            throw new APIException("No category created till now or pageNumber doesn't exists!");
        }

        return modelMapper.map(categoryPage, CategoryResponseDTO.class);

    }

    @Override
    public CategoryDTO  addCategory(CategoryDTO categoryDto) {

        Category savedCategory=categoryRepository.findByCategoryName(categoryDto.getName());
        if(savedCategory!=null){
            throw new APIException("Category with categoryName: "+categoryDto.getName()+" already exists");
        }

        savedCategory=categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        Category res=categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",id));

        categoryRepository.deleteById(id);
        return modelMapper.map(res, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDto, Long id) {
       categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",id));

        Category res=categoryRepository.findByCategoryName(categoryDto.getName());

        if(res!=null && !res.getCategoryId().equals(id)) {
            throw new APIException("Category with categoryName: "+categoryDto.getName()+" already exists");
        }

        res=modelMapper.map(categoryDto, Category.class);
        res.setCategoryId(id);
        res=categoryRepository.save(res);
        return modelMapper.map(res, CategoryDTO.class);
    }

}

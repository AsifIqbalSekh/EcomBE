package com.asifiqbalsekh.EcomBE.service.implementation;

import com.asifiqbalsekh.EcomBE.config.AppConstant;
import com.asifiqbalsekh.EcomBE.config.AuthUtil;
import com.asifiqbalsekh.EcomBE.dto.ProductDTO;
import com.asifiqbalsekh.EcomBE.dto.ProductResponseDTO;
import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.Cart;
import com.asifiqbalsekh.EcomBE.model.Category;
import com.asifiqbalsekh.EcomBE.model.Product;
import com.asifiqbalsekh.EcomBE.repository.CartRepository;
import com.asifiqbalsekh.EcomBE.repository.CategoryRepository;
import com.asifiqbalsekh.EcomBE.repository.ProductRepository;
import com.asifiqbalsekh.EcomBE.service.CartService;
import com.asifiqbalsekh.EcomBE.service.FileService;
import com.asifiqbalsekh.EcomBE.service.OrderService;
import com.asifiqbalsekh.EcomBE.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final CartService cartService;
    private final OrderService orderService;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Product savedProduct =productRepository.findByProductNameIgnoreCase(productDTO.getName());
        if(savedProduct !=null){
            throw new APIException("Product with Name: "+productDTO.getName()+" already exists");
        }


        Product product=modelMapper.map(productDTO, Product.class);

        product.setCategory(category);
        product.setImage("default.png");
        product.setSpecialPrice(0D);
        product.setUser(authUtil.userDetails());

        product=productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);

    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(Sort.Direction.ASC,sortBy):
                Sort.by(Sort.Direction.DESC,sortBy);

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByOrder);

        Page<Product> productPage=productRepository.findAll(pageDetails);
        if(productPage.getContent().isEmpty()){
            throw new APIException("No product created till now or pageNumber doesn't exists!");
        }

        return modelMapper.map(productPage,ProductResponseDTO.class);

    }

    @Override
    public ProductResponseDTO getAllProductsUsingCategory(
            Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder
    ) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        Sort sortByOrder= sortOrder.equalsIgnoreCase("asc")?Sort.by(Sort.Direction.ASC,sortBy):
                Sort.by(Sort.Direction.DESC,sortBy);
        Pageable pageDetails=PageRequest.of(pageNumber,pageSize,sortByOrder);

        Page<Product> productPage=productRepository.findByCategory(category,pageDetails);
        if(productPage.getContent().isEmpty()){
            throw new APIException("No product found for categoryId:"+categoryId+" or pageNumber doesn't exists!");
        }
        return modelMapper.map(productPage,ProductResponseDTO.class);

    }

    @Override
    public ProductResponseDTO getSearchProductsUsingKeyword(
            String keyWord, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(Sort.Direction.ASC,sortBy):
                Sort.by(Sort.Direction.DESC,sortBy);
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> productPage=productRepository.findByProductNameContainingIgnoreCase(keyWord,pageDetails);
        if(productPage.getContent().isEmpty()){
            throw new APIException("No product found for KeyWord:"+keyWord+" or pageNumber doesn't exists!");
        }

        return modelMapper.map(productPage,ProductResponseDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO givenProductDTO) {

        Product savedProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        Product res =productRepository.findByProductNameIgnoreCase(givenProductDTO.getName());
        if(res !=null && !res.getProductId().equals(productId)){
            throw new APIException("Product with Name: "+givenProductDTO.getName()+" already exists");
        }


        Product givenProduct=modelMapper.map(givenProductDTO,Product.class);

        savedProduct.setProductName(givenProduct.getProductName());
        savedProduct.setDescription(givenProduct.getDescription());
        savedProduct.setQuantity(givenProduct.getQuantity());

        savedProduct.setPrice(givenProduct.getPrice());
        savedProduct.setDiscount(givenProduct.getDiscount());
        savedProduct.setSpecialPrice(0D);

        savedProduct=productRepository.save(savedProduct);
        ProductDTO savedProductDTO=modelMapper.map(savedProduct, ProductDTO.class);

        //Updating the carts which related to this product

        cartService.updateProductInCart(savedProductDTO);

        return savedProductDTO;

    }

    @Override
    @Transactional
    public ProductDTO deleteProduct(Long productId) {

        Product savedProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));


        //Deleting the product

        productRepository.delete(savedProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new APIException("Image is required and cannot be empty.");
        }

        Product savedProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        String fileName= fileService.uploadProductImage(AppConstant.PRODUCT_IMAGE_PATH,image);

        savedProduct.setImage(fileName);

        savedProduct=productRepository.save(savedProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }
}

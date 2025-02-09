package com.asifiqbalsekh.EcomBE.service.implementation;

import com.asifiqbalsekh.EcomBE.config.AuthUtil;
import com.asifiqbalsekh.EcomBE.dto.CartItemResponseDTO;
import com.asifiqbalsekh.EcomBE.dto.CartResponseDTO;
import com.asifiqbalsekh.EcomBE.dto.ProductDTO;
import com.asifiqbalsekh.EcomBE.dto.UserInfoResponseDTO;
import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.Cart;
import com.asifiqbalsekh.EcomBE.model.CartItem;
import com.asifiqbalsekh.EcomBE.model.Product;
import com.asifiqbalsekh.EcomBE.repository.CartItemRepository;
import com.asifiqbalsekh.EcomBE.repository.CartRepository;
import com.asifiqbalsekh.EcomBE.repository.ProductRepository;
import com.asifiqbalsekh.EcomBE.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;


    @Override
    public CartResponseDTO addProduct(Long productId, Integer quantity) {

        //Checking Product Exist or Not
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //Validation
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        //Finding or Creating Cart for user
        Cart myCart=createOrFindCart();

        //Finding Product Already Exists in the cart or not
        CartItem myCartItem =cartItemRepository.findCartItemByCartAndProduct(myCart.getCartId(),productId);

        if(myCartItem !=null){
            throw new APIException("Product already exists in the Cart");
        }

        myCartItem=new CartItem();

        myCartItem.setProduct(product);
        myCartItem.setQuantity(quantity);
        myCartItem.setCart(myCart);
        myCartItem.setDiscount(product.getDiscount());
        myCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(myCartItem);

        myCart.setTotalPrice(myCart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        myCart.getCartItems().add(myCartItem);

        cartRepository.save(myCart);

        //Preparing response

        return getCartResponseDTO(myCart);

    }

    @Override
    public List<CartResponseDTO> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new APIException("No carts found");
        }

        return carts.stream().map(this::getCartResponseDTO).collect(toList());
    }

    @Override
    public CartResponseDTO getUserCart() {
        UserInfoResponseDTO userInfo=authUtil.userInfoDetails();
        Cart cart=cartRepository.findCartByEmail(userInfo.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "email", userInfo.getEmail()));
        return getCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartItemQuantity(Long productId, Integer quantity) {
        UserInfoResponseDTO userInfo=authUtil.userInfoDetails();

        Cart cart=cartRepository.findCartByEmail(userInfo.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "email", userInfo.getEmail())
        );

        //Checking Product Exist or Not
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        //Validation
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        //Finding Product Exists in the cart or not
        CartItem myCartItem =cartItemRepository.findCartItemByCartAndProduct(cart.getCartId(),productId);

        if(myCartItem==null){
            throw new APIException("Product does not exists in the Cart!");
        }

        int newQuantity = myCartItem.getQuantity()+quantity;

        if(newQuantity==0){
            deleteProductFromCart(cart.getCartId(),productId);
        }
        else{
            myCartItem.setQuantity(newQuantity);
            cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
            cart=cartRepository.save(cart);
            cartItemRepository.save(myCartItem);

        }


        return getCartResponseDTO(cart);
    }

    @Transactional
    @Override
    public CartResponseDTO deleteProductFromCart(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", cartId)
        );
        //Finding Product Exists in the cart or not
        CartItem myCartItem =cartItemRepository.findCartItemByCartAndProduct(cart.getCartId(),productId);

        if(myCartItem==null){
            throw new APIException("Product does not exists in the Cart!");
        }
        cart.setTotalPrice(cart.getTotalPrice()-(myCartItem.getProductPrice()*myCartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        return getCartResponseDTO(cart);

    }

    @Override
    public void updateProductInCart(ProductDTO updatedProductDTO) {
        List<Cart> carts = cartRepository.findAllCartByProductId(updatedProductDTO.getId());
        if(carts.isEmpty()){
            return;
        }
        carts.forEach(item -> {

            Long cartId = item.getCartId();
            Long productId=updatedProductDTO.getId();

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

            CartItem cartItem = cartItemRepository.findCartItemByCartAndProduct(cartId, productId);

            if (cartItem == null) {
                throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
            }

            double cartPrice = cart.getTotalPrice()
                    - (cartItem.getProductPrice() * cartItem.getQuantity());

            cartItem.setProductPrice(product.getSpecialPrice());

            cart.setTotalPrice(cartPrice
                    + (cartItem.getProductPrice() * cartItem.getQuantity()));

            cartItemRepository.save(cartItem);
        });
    }

    private CartResponseDTO getCartResponseDTO(Cart myCart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartId(myCart.getCartId());
        cartResponseDTO.setTotalPrice(myCart.getTotalPrice());


        List<CartItem> cartItems = myCart.getCartItems();


        List<CartItemResponseDTO> cartItemResponseDTO =cartItems.stream().map(item->{

            CartItemResponseDTO ele=new CartItemResponseDTO();
            ele.setCartItemId(item.getCartItemId());
            ele.setDiscount(item.getDiscount());
            ele.setQuantity(item.getQuantity());
            ele.setProductPrice(item.getProductPrice());
            ele.setProductDTO(modelMapper.map(item.getProduct(), ProductDTO.class));
            return ele;

        }).collect(toList());
        cartResponseDTO.setCartItems(cartItemResponseDTO);

        return cartResponseDTO;
    }

    private Cart createOrFindCart() {

        return cartRepository.findCartByEmail(authUtil.userInfoDetails().getEmail())
                .orElseGet(()-> {
                        Cart cart = new Cart();
                        cart.setTotalPrice(0.00);
                        cart.setUser(authUtil.userDetails());
                    return cartRepository.save(cart);

        });
    }
}

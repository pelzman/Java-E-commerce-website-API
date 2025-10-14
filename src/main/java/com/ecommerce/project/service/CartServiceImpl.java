package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements  CartService{

    @Autowired
    CartRepository cartRepository;
     @Autowired
     ProductRepository productRepository;
     @Autowired
    CartItemRepository cartItemRepository;
     @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtils authUtils;

    @Transactional
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart  cart = createCart();

        //Retrieve all the products
        Product productDetail = productRepository.findById(productId)
                .orElseThrow( () ->new ResourceNotFoundException("Product","productId", productId));


        //validations
       CartItem cartItem =  cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

       if(cartItem != null){
           throw  new APIException("Product " + productDetail.getProductName() + "already exist in the cart" );
       }

       if(productDetail.getQuantity() == 0){
           throw  new APIException(productDetail.getProductName() + "not available currently");
       }
        if(productDetail.getQuantity() < quantity){
            throw  new APIException("Please, make an order of the " + productDetail.getProductName() +
                    " less than or equal to the quantity " + productDetail.getQuantity());
        }

        //create the cartItems
        CartItem newCartItem = new CartItem();
            newCartItem.setProduct(productDetail);
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);
            newCartItem.setDiscount(productDetail.getDiscount());
            newCartItem.setProductPrice(productDetail.getSpecialPrice());
           CartItem savedCartItem =  cartItemRepository.save(newCartItem);
           //depending on the requirement , if we want to the reduce the stock as the quantity is added , we can subtract the quantity from the existing quantity
//            productDetail.setQuantity(productDetail.getQuantity());

            cart.getCartItems().add(savedCartItem);
            //update the total price of the product
            recalculateCartTotalPrice(cart);
          //save the cart after updating
           cartRepository.save(cart);
        // return updated cart
        CartDTO  cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setTotaPrice(cart.getTotalPrice());
           List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item ->{
           ProductDTO map  =  modelMapper.map(item.getProduct(), ProductDTO.class);
           map.setQuantity(item.getQuantity());
           return  map;
        });
         cartDTO.setProducts(productDTOStream.toList());

        return  cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.size() == 0 ) {
            throw new APIException("No cart exist");
        }

        List<CartDTO> cartDTOS = carts.stream().map(cart ->{
         CartDTO cartDTO =    modelMapper.map(cart , CartDTO.class);
         cartDTO.setTotaPrice(cart.getTotalPrice());
            cart.getCartItems().forEach( c -> c.getProduct().setQuantity(c.getQuantity()));
         List<ProductDTO> products = cart.getCartItems().stream().map(p ->modelMapper.map(p.getProduct(), ProductDTO.class))
                         .collect(Collectors.toList());
            cartDTO.setProducts(products);
              return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {

        Cart cart = cartRepository.findCartByEmailANDCartId(emailId, cartId);


        if(cart == null){
            throw  new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO  cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setTotaPrice(cart.getTotalPrice());
      cart.getCartItems().forEach( c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream().map(
                p -> modelMapper.map(p.getProduct(), ProductDTO.class)
        ).collect(Collectors.toList());
        cartDTO.setProducts(products);
        return cartDTO;
    }

    private Cart createCart() {
        //find cart for the user or create new one
        Cart userCart = cartRepository.findCartByEmail(authUtils.loggedInEmail());

        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
//        cart.setTotalPrice(0.00);
        cart.setUser(authUtils.loggedInUser());
          Cart newCart = cartRepository.save(cart);
        return newCart;
    }


    private void recalculateCartTotalPrice(Cart cart) {
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            cart.setTotalPrice(0.0);
            return;
        }
        // Ensure cart.getCartItems() is not null and is populated (e.g., eager loading or fetched separately)
        double newTotalPrice = cart.getCartItems().stream()
                .filter(item -> item != null && item.getProductPrice() != null)
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        System.out.println(newTotalPrice + " {} new price");
        cart.setTotalPrice(newTotalPrice);

    }

}

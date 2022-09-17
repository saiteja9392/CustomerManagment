package com.demo.service.order;

import com.demo.entity.Product;
import com.demo.entity.order.Cart;
import com.demo.entity.order.Wishlist;
import com.demo.exception.custom.ResourceException;
import com.demo.model.order.CartDetails;
import com.demo.repository.ProductRepo;
import com.demo.repository.order.CartRepo;
import com.demo.repository.order.WishlistRepo;
import com.demo.response.Response;
import com.demo.validation.CustomerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CustomerValidation customerValidation;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    WishlistRepo wishlistRepo;

    public Response viewCart(String loginId) {

        customerValidation.validateCustomer(loginId);

        List<Cart> byLoginId = cartRepo.findByLoginId(loginId);

        return Response.buildResponse("Cart Details",byLoginId);
    }

    @Transactional
    public Response addToCart(String loginId, CartDetails cartDetails) {

        customerValidation.validateCustomer(loginId);

        Optional<Product> findProduct = productRepo.findById(cartDetails.getProductId());

        if(!findProduct.isPresent())
            throw new ResourceException("In-Valid Product");

        if(findProduct.get().getQuantityInStore() <= 0)
            throw new ResourceException("Out Of Stock");

        if(findProduct.get().getQuantityInStore() < cartDetails.getQuantity())
            throw new ResourceException(String.format("Available Stock In Store %s, Please Select Accordingly",findProduct.get().getQuantityInStore()));

        Optional<Cart> byProductIdInCart = cartRepo.findByProductId(cartDetails.getProductId());

        Cart updatedCart;

        if(byProductIdInCart.isPresent()){

            Integer currentQuantity = byProductIdInCart.get().getQuantity();
            byProductIdInCart.get().setQuantity(currentQuantity + cartDetails.getQuantity());

            updatedCart = cartRepo.save(byProductIdInCart.get());
        }

        else {

            Cart cart = new Cart();

            cart.setLoginId(loginId);
            cart.setProductId(cartDetails.getProductId());
            cart.setProductName(findProduct.get().getProductName());
            cart.setQuantity(cartDetails.getQuantity());

            cart.setTransactionId(cart.getTransactionId());

            updatedCart = cartRepo.save(cart);
        }

        return Response.buildResponse("Added To Cart",updatedCart);
    }

    @Transactional
    public Response moveToWishList(String loginId, String cartTransactionId) {

        customerValidation.validateCustomer(loginId);

        Optional<Cart> cartById = cartRepo.findById(cartTransactionId);

        if(!cartById.isPresent())
            throw new ResourceException("Product Not Present In Cart");

        Wishlist wishlist = new Wishlist();

        wishlist.setLoginId(loginId);
        wishlist.setProductId(cartById.get().getProductId());
        wishlist.setProductName(cartById.get().getProductName());
        wishlist.setQuantity(cartById.get().getQuantity());
        wishlist.setTransactionId(wishlist.getTransactionId());

        cartRepo.delete(cartById.get());

        return Response.buildResponse("Moved To Wishlist", wishlistRepo.save(wishlist));
    }
}

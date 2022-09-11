package com.demo.service.order;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.order.Cart;
import com.demo.entity.order.Wishlist;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.order.CartRepo;
import com.demo.repository.order.WishlistRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    WishlistRepo wishlistRepo;

    @Autowired
    CartRepo cartRepo;

    @Transactional
    public Response moveToCart(String loginId, String wishListTransactionId) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent()|| !customerLoginInfo.isPresent())
            throw new ResourceException("No Customer Found");

        Optional<Wishlist> wishListById = wishlistRepo.findById(wishListTransactionId);

        if(!wishListById.isPresent())
            throw new ResourceException("Product Not Present In Wishlist");

        Cart cart = new Cart();

        cart.setLoginId(loginId);
        cart.setProductId(wishListById.get().getProductId());
        cart.setProductName(wishListById.get().getProductName());
        cart.setQuantity(wishListById.get().getQuantity());
        cart.setTransactionId(cart.getTransactionId());

        wishlistRepo.delete(wishListById.get());

        return Response.buildResponse("Moved To Cart", cartRepo.save(cart));
    }

    public Response viewWishList(String loginId) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent()|| !customerLoginInfo.isPresent())
            throw new ResourceException("No Customer Found");

        List<Wishlist> wishListById = wishlistRepo.findByLoginId(loginId);

        return Response.buildResponse("Wishlist Details",wishListById);
    }
}

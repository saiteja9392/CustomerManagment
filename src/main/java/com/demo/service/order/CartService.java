package com.demo.service.order;

import com.demo.entity.Cart;
import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.Product;
import com.demo.exception.custom.ResourceException;
import com.demo.model.order.CartDetails;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.ProductRepo;
import com.demo.repository.order.CartRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    ProductRepo productRepo;

    public Response viewCart(String loginId) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent()|| !customerLoginInfo.isPresent())
            throw new ResourceException("No Customer Found");

        List<Cart> byLoginId = cartRepo.findByLoginId(loginId);

        return Response.buildResponse("Cart Details",byLoginId);
    }

    @Transactional
    public Response addToCart(String loginId, CartDetails cartDetails) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent()|| !customerLoginInfo.isPresent())
            throw new ResourceException("No Customer Found");

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
}

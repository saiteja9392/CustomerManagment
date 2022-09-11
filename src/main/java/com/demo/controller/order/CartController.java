package com.demo.controller.order;

import com.demo.entity.order.Cart;
import com.demo.model.order.CartDetails;
import com.demo.response.Response;
import com.demo.service.order.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/ViewCart/{loginId}")
    private List<CollectionModel> viewCart(@PathVariable String loginId){

        Response response = cartService.viewCart(loginId);

        List<Cart> cartList = (List<Cart>) response.getEntity();

        CollectionModel<Cart> model = null;

        List<CollectionModel> models = new ArrayList<>();

        for(Cart cart : cartList){

            model = CollectionModel.of(Collections.singleton(cart));
            WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).moveToWishList(loginId,cart.getTransactionId()));
            model.add(linkTo.withRel("move-to-wishlist"));

            models.add(model);
        }

        return models;
    }

    @PostMapping("/AddToCart/{loginId}")
    private ResponseEntity<Response> addToCart(@PathVariable String loginId, @RequestBody CartDetails cartDetails){

        return new ResponseEntity<>(cartService.addToCart(loginId,cartDetails), HttpStatus.CREATED);
    }

    @PostMapping("/MoveToWishList")
    public ResponseEntity<Response> moveToWishList(@RequestParam String loginId, @RequestParam String cartTransactionId){

        return new ResponseEntity<>(cartService.moveToWishList(loginId, cartTransactionId), HttpStatus.CREATED);
    }
}

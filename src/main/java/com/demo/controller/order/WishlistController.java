package com.demo.controller.order;

import com.demo.entity.order.Wishlist;
import com.demo.response.Response;
import com.demo.service.order.WishlistService;
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
@RequestMapping("/Wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @GetMapping("/ViewWishList/{loginId}")
    public List<CollectionModel> viewWishList(@PathVariable String loginId){

        Response response = wishlistService.viewWishList(loginId);

        List<Wishlist> wishlistList = (List<Wishlist>) response.getEntity();

        CollectionModel<Wishlist> model = null;

        List<CollectionModel> models = new ArrayList<>();

        for(Wishlist wishlist : wishlistList){

            model = CollectionModel.of(Collections.singleton(wishlist));
            WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).moveToCart(loginId,wishlist.getTransactionId()));
            model.add(linkTo.withRel("move-to-cart"));

            models.add(model);
        }

        return models;
    }

    @PostMapping("/MoveToCart")
    public ResponseEntity<Response> moveToCart(@RequestParam String loginId, @RequestParam String wishListTransactionId){

        return new ResponseEntity<>(wishlistService.moveToCart(loginId, wishListTransactionId), HttpStatus.CREATED);
    }
}

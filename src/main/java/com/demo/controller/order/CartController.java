package com.demo.controller.order;

import com.demo.model.order.CartDetails;
import com.demo.response.Response;
import com.demo.service.order.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/ViewCart/{loginId}")
    private ResponseEntity<Response> viewCart(@PathVariable String loginId){

        return ResponseEntity.ok(cartService.viewCart(loginId));
    }

    @PostMapping("/AddToCart/{loginId}")
    private ResponseEntity<Response> addToCart(@PathVariable String loginId, @RequestBody CartDetails cartDetails){

        return new ResponseEntity<>(cartService.addToCart(loginId,cartDetails), HttpStatus.CREATED);
    }
}

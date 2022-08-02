package com.demo.controller;

import com.demo.entity.Offer;
import com.demo.model.OfferRequest;
import com.demo.response.Response;
import com.demo.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Offer")
public class OfferController {

    @Autowired
    OfferService offerService;

    @PostMapping("/AddOffer")
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer){

        return new ResponseEntity<>(offerService.addOffer(offer),HttpStatus.CREATED);
    }

    @PostMapping("/AddOfferToProduct")
    public ResponseEntity<Response> addOfferToProduct(@RequestBody OfferRequest offerRequest){

        return new ResponseEntity<>(offerService.addOfferToProduct(offerRequest),HttpStatus.CREATED);
    }

    @DeleteMapping("/RemoveOfferForProduct/{productId}")
    public ResponseEntity<Response> removeOfferFromProduct(@PathVariable String productId){

        return new ResponseEntity<>(offerService.removeOfferForProduct(productId),HttpStatus.OK);
    }
}

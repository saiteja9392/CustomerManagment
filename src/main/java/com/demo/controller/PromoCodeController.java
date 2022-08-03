package com.demo.controller;

import com.demo.entity.PromoCode;
import com.demo.response.Response;
import com.demo.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/PromoCode")
public class PromoCodeController {

    @Autowired
    PromoCodeService promoCodeService;

    @PostMapping("/AddPromoCode")
    public ResponseEntity<PromoCode> addPromoCode(@RequestBody PromoCode promoCode){

        return new ResponseEntity<>(promoCodeService.addPromoCode(promoCode), HttpStatus.CREATED);
    }

    @GetMapping("/AllPromoCodes")
    public List<PromoCode> getAllPromoCodes(){

        return promoCodeService.getAllPromoCodes();
    }

    @PutMapping("/EnablePromoCode/{code}")
    public ResponseEntity<Response> enablePromoCode(@PathVariable String code){

        return new ResponseEntity<>(promoCodeService.enablePromoCode(code), HttpStatus.OK);
    }

    @PutMapping("/DisablePromoCode/{code}")
    public ResponseEntity<Response> disablePromoCode(@PathVariable String code){

        return new ResponseEntity<>(promoCodeService.disablePromoCode(code), HttpStatus.OK);
    }

    @DeleteMapping("/DeletePromoCode/{code}")
    public ResponseEntity<Response> deletePromoCode(@PathVariable String code){

        return new ResponseEntity<>(promoCodeService.deletePromoCode(code), HttpStatus.OK);
    }
}

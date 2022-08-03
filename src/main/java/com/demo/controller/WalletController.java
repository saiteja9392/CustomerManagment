package com.demo.controller;

import com.demo.entity.Wallet;
import com.demo.response.Response;
import com.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/AddWallet")
    public ResponseEntity<Response> addWallet(@RequestBody Wallet wallet){

        return new ResponseEntity<>(walletService.addWallet(wallet), HttpStatus.CREATED);
    }

    @PostMapping("/AddMoneyToWallet")
    public ResponseEntity<Response> addMoneyToWallet(@RequestParam String walletId, @RequestParam Integer addMoney){

        return new ResponseEntity<>(walletService.addMoneyToWallet(walletId, addMoney), HttpStatus.OK);
    }

    @GetMapping("/CheckBalance/{walletId}")
    public Object checkBalance(@PathVariable String walletId){

        Response walletDetails = walletService.checkBalance(walletId);

        EntityModel<Response> model = EntityModel.of(walletDetails);

        Wallet wallet = (Wallet) walletDetails.getEntity();

        if(wallet.getBalance() == 0){

            WebMvcLinkBuilder linkToAddMoney = linkTo(methodOn(this.getClass()).addMoneyToWallet(walletId,100));
            model.add(linkToAddMoney.withRel("add-money"));

            return model;
        }
        else
            return walletDetails;
    }

    @DeleteMapping("/DeleteWallet/{walletId}")
    public Response deleteWallet(@PathVariable String walletId){

        return walletService.deleteWallet(walletId);
    }

    @PutMapping("/DisableWallet/{walletId}")
    public Response disableWallet(@PathVariable String walletId){

        return walletService.disableWallet(walletId);
    }

    @PutMapping("/EnableWallet/{walletId}")
    public Response enableWallet(@PathVariable String walletId){

        return walletService.enableWallet(walletId);
    }
}

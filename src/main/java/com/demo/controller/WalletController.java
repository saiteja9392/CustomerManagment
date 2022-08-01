package com.demo.controller;

import com.demo.entity.CustomerLogin;
import com.demo.entity.Wallet;
import com.demo.response.Response;
import com.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/AddWallet")
    public ResponseEntity<CustomerLogin> addWallet(@RequestBody Wallet wallet){

        return new ResponseEntity<>(walletService.addWallet(wallet), HttpStatus.CREATED);
    }

    @PostMapping("/AddMoneyToWallet")
    public ResponseEntity<Wallet> addMoneyToWallet(@RequestParam String walletId, @RequestParam Integer addMoney){

        return new ResponseEntity<>(walletService.addMoneyToWallet(walletId, addMoney), HttpStatus.OK);
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

package com.demo.controller.wallet;

import com.demo.response.Response;
import com.demo.service.wallet.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/WalletTransaction")
public class WalletTransactionController {

    @Autowired
    WalletTransactionService walletTransactionService;

    @GetMapping("/GetCustomerWalletTransactions/{loginId}")
    public ResponseEntity<Response> getCustomerWalletTransactions(@PathVariable String loginId){

        return new ResponseEntity<>(walletTransactionService.getCustomerWalletTransactions(loginId), HttpStatus.OK);
    }

    @GetMapping("/GetCustomerWalletTransactionsByType")
    public ResponseEntity<Response> getCustomerWalletTransactionsByType(@RequestParam String loginId,@RequestParam Boolean type){

        return new ResponseEntity<>(walletTransactionService.getCustomerWalletTransactionsByType(loginId,type), HttpStatus.OK);
    }
}

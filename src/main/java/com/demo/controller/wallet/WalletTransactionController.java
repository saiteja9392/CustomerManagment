package com.demo.controller.wallet;

import com.demo.response.Response;
import com.demo.service.wallet.WalletTransactionService;
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
@RequestMapping("/WalletTransaction")
public class WalletTransactionController {

    @Autowired
    WalletTransactionService walletTransactionService;

    @GetMapping("/GetWalletTransaction/{transactionId}")
    public ResponseEntity<Response> getWalletTransaction(@PathVariable String transactionId){

        return new ResponseEntity<>(walletTransactionService.getWalletTransaction(transactionId), HttpStatus.OK);
    }

    @GetMapping("/GetCustomerWalletTransactions/{loginId}")
    public ResponseEntity<Response> getCustomerWalletTransactions(@PathVariable String loginId){

        return new ResponseEntity<>(walletTransactionService.getCustomerWalletTransactions(loginId), HttpStatus.OK);
    }

    @GetMapping("/GetCustomerWalletTransactionsByType")
    public ResponseEntity<Response> getCustomerWalletTransactionsByType(@RequestParam String loginId,@RequestParam Boolean type){

        return new ResponseEntity<>(walletTransactionService.getCustomerWalletTransactionsByType(loginId,type), HttpStatus.OK);
    }

    @GetMapping("/GetRefundTransactions/{loginId}")
    public List<CollectionModel> getRefundTransactions(@PathVariable String loginId ){

        List<String> refundTransactionIds = walletTransactionService.getRefundTransactions(loginId);

        List<CollectionModel> models = new ArrayList<>();

        for(String transactionId : refundTransactionIds){

            CollectionModel<String> model = CollectionModel.of(Collections.singleton(transactionId));
            WebMvcLinkBuilder linkToViewTransaction = linkTo(methodOn(this.getClass()).getWalletTransaction(transactionId));
            model.add(linkToViewTransaction.withRel("view-transaction"));

            models.add(model);
        }

        return models;
    }

    @GetMapping("/GetRechargeTransactions/{loginId}")
    public List<CollectionModel> getRechargeTransactions(@PathVariable String loginId ){

        List<String> rechargeTransactionIds = walletTransactionService.getRechargeTransactions(loginId);

        List<CollectionModel> models = new ArrayList<>();

        rechargeTransactionIds.forEach(transactionId -> {

            CollectionModel<String> model = CollectionModel.of(Collections.singleton(transactionId));
            WebMvcLinkBuilder linkToViewTransaction = linkTo(methodOn(this.getClass()).getWalletTransaction(transactionId));
            model.add(linkToViewTransaction.withRel("view-transaction"));

            models.add(model);
        });

        return models;
    }
}

package com.demo.controller;

import com.demo.entity.Refund;
import com.demo.response.Response;
import com.demo.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Refund")
public class RefundController {

    @Autowired
    RefundService refundService;

    @PostMapping("/InitiateRefund/{transactionId}")
    public ResponseEntity<Response> initiateRefund(@PathVariable String transactionId){

        return new ResponseEntity<>(refundService.initiateRefund(transactionId), HttpStatus.OK);
    }

    @GetMapping("/GetCustomerRefundDetails/{customerId}")
    public ResponseEntity<List<Refund>> getRefundDetailsOfCustomer(@PathVariable String customerId){

        return new ResponseEntity<>(refundService.getRefundDetailsOfCustomer(customerId),HttpStatus.OK);
    }
}

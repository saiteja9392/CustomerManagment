package com.demo.controller;

import com.demo.model.FullInformation;
import com.demo.service.CustomerFullInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerFullInformationController {


    @Autowired
    CustomerFullInformationService customerFullInformationService;

    @GetMapping("/CustomerFullInformation/{customerId}")
    public ResponseEntity<FullInformation> getCustomerFullInformation(@PathVariable String customerId){

        return new ResponseEntity<>(customerFullInformationService.getCustomerFullInformation(customerId), HttpStatus.OK);

    }
}

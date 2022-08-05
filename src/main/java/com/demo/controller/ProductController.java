package com.demo.controller;

import com.demo.entity.Product;
import com.demo.model.ProductDetails;
import com.demo.response.Response;
import com.demo.service.ProductService;
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
@RequestMapping("/Product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/AddProduct")
    public ResponseEntity<Response> addProduct(@RequestBody Product newProduct){

        return new ResponseEntity<>(productService.addProduct(newProduct), HttpStatus.CREATED);
    }

    @PutMapping("/UpdateProductInformation")
    public ResponseEntity<Response> updateProductInformation(@RequestParam String productId, @RequestBody Product updatedProduct){

        return new ResponseEntity<>(productService.updateProductInformation(productId,updatedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/DeleteProduct/{productId}")
    public ResponseEntity<Response> deleteProductInformation(@PathVariable String productId){

        return new ResponseEntity<>(productService.deleteProductInformation(productId), HttpStatus.OK);
    }

    @PostMapping("/AddStockToStore")
    public ResponseEntity<Response> addStockToStore(@RequestParam String productId, @RequestParam Integer quantity){

        return new ResponseEntity<>(productService.addStockToStore(productId,quantity), HttpStatus.OK);
    }

    @GetMapping("/CurrentProductsInStore")
    public ResponseEntity<List<ProductDetails>> currentProductsInStore(){

        return new ResponseEntity<>(productService.currentProductsInStore(), HttpStatus.OK);
    }

    @GetMapping("/FilterBasedOnQuantity")
    public ResponseEntity<List<ProductDetails>> filterBasedOnQuantity(@RequestParam Integer quantity){

        return new ResponseEntity<>(productService.filterBasedOnQuantity(quantity), HttpStatus.OK);
    }

    @GetMapping("/ProductsBasedOnStock")
    public List<CollectionModel> filterProductsBasedOnStock(@RequestParam boolean stockStatus){

        List<ProductDetails> productDetails = productService.filterProductsBasedOnStock(stockStatus);

        CollectionModel<ProductDetails> model = null;

        List<CollectionModel> models = new ArrayList<>();

        if(!stockStatus){

            for (ProductDetails product : productDetails) {
                model = CollectionModel.of(Collections.singleton(product));
                WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).addStockToStore(product.getProductId(), product.getQuantityInStore()));
                model.add(linkTo.withRel("add-stock-to-store"));

                models.add(model);
            }

        }

        else{

            for (ProductDetails product : productDetails) {
                model = CollectionModel.of(Collections.singleton(product));

                models.add(model);
            }
        }

        return models;
    }

    @PutMapping("/MakeProductOutOfStock/{productId}")
    public ResponseEntity<Response> makeProductOutOfStock(@PathVariable String productId){

        return new ResponseEntity<>(productService.makeProductOutOfStock(productId),HttpStatus.ACCEPTED);
    }
}

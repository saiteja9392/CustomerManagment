package com.demo.service;

import com.demo.entity.Product;
import com.demo.exception.custom.ResourceException;
import com.demo.model.ProductDetails;
import com.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    private String Status = "";

    public Product addProduct(Product newProduct) {

        Optional<Product> product = productRepo.findById(newProduct.getProductId());

        if(product.isPresent()){

            throw new ResourceException("Product Id already exists");
        }

        return productRepo.save(newProduct);
    }

    public Product updateProductInformation(String productId, Product updatedProduct) {

        Optional<Product> product = productRepo.findById(productId);

        if(!product.isPresent()){

            throw new ResourceException("Product Not Found");
        }

        product.get().setProductName(updatedProduct.getProductName());
        product.get().setPrice(updatedProduct.getPrice());
        product.get().setManufacturer(updatedProduct.getManufacturer());

        return productRepo.save(product.get());
    }

    public String addStockToStore(String productId, Integer quantity) {

        Optional<Product> product = productRepo.findById(productId);

        if(!product.isPresent()){

            throw new ResourceException("Product Not Found");
        }

        int newCount = product.get().getQuantityInStore() + quantity;

        product.get().setQuantityInStore(newCount);

        Product updatedProductDetails = productRepo.save(product.get());

        Status = String.format("New Stock Added, Total Quantity of %s In Store %s",
                                product.get().getProductName(),updatedProductDetails.getQuantityInStore());

        return Status;
    }

    public List<ProductDetails> currentProductsInStore() {

        List<Product> allProductsInStore = productRepo.findAll();

        List<ProductDetails> productDetails = new ArrayList<>();

        allProductsInStore.forEach(product ->{

            ProductDetails productDetail = new ProductDetails();

            productDetail.setProductId(product.getProductId());
            productDetail.setProductName(product.getProductName());
            productDetail.setQuantityInStore(product.getQuantityInStore());

            productDetails.add(productDetail);
        });

        return productDetails;
    }

    public List<ProductDetails> filterProductsBasedOnStock(boolean stockStatus) {

        List<Product> allProductsInStore = productRepo.findAll();

        List<ProductDetails> productDetails = new ArrayList<>();

        List<Product> stock;

        if(stockStatus)
            stock = allProductsInStore.stream().filter(product -> product.getQuantityInStore() > 0).collect(Collectors.toList());

        else{
            stock = allProductsInStore.stream().filter(product -> product.getQuantityInStore() <= 0).collect(Collectors.toList());
        }

        stock.forEach(product ->{

            ProductDetails productDetail = new ProductDetails();

            productDetail.setProductId(product.getProductId());
            productDetail.setProductName(product.getProductName());
            productDetail.setQuantityInStore(product.getQuantityInStore());

            productDetails.add(productDetail);
        });

        Collections.sort(productDetails, Comparator.comparing(ProductDetails::getProductId));

        return productDetails;
    }

    public String deleteProductInformation(String productId) {

        Optional<Product> product = productRepo.findById(productId);

        if(!product.isPresent()){

            throw new ResourceException("Product Not Found");
        }

        productRepo.deleteById(productId);

        Status = String.format("Product '%s' Has Been Deleted Successfully",product.get().getProductName());

        return Status;
    }

    public List<ProductDetails> filterBasedOnQuantity(Integer quantity) {

        List<Product> allProducts = productRepo.findAll();

        List<Product> filteredProducts = allProducts.stream().filter(p -> p.getQuantityInStore() >= quantity).collect(Collectors.toList());

        List<ProductDetails> productDetails = new ArrayList<>();

        filteredProducts.forEach(p -> {

            ProductDetails product = new ProductDetails();
            product.setProductId(p.getProductId());
            product.setProductName(p.getProductName());
            product.setQuantityInStore(p.getQuantityInStore());

            productDetails.add(product);
        });

        Collections.sort(productDetails, Comparator.comparing(ProductDetails::getQuantityInStore));

        return productDetails;
    }
}

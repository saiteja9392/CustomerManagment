package com.demo.service;

import com.demo.entity.Product;
import com.demo.exception.custom.ResourceException;
import com.demo.model.ProductDetails;
import com.demo.repository.ProductRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    public Response addProduct(Product newProduct) {

        Optional<Product> product = productRepo.findById(newProduct.getProductId());

        if(product.isPresent()){

            throw new ResourceException("Product Id already exists");
        }

        return Response.buildResponse("Product Added",productRepo.save(newProduct));
    }

    public Response updateProductInformation(String productId, Product updatedProduct) {

        Optional<Product> product = productRepo.findById(productId);

        if(!product.isPresent()){

            throw new ResourceException("Product Not Found");
        }

        product.get().setProductName(updatedProduct.getProductName());
        product.get().setPrice(updatedProduct.getPrice());
        product.get().setManufacturer(updatedProduct.getManufacturer());

        return Response.buildResponse("Product Information Updated",productRepo.save(product.get()));
    }

    public Response addStockToStore(String productId, Integer quantity) {

        Optional<Product> product = productRepo.findById(productId);

        if(!product.isPresent()){

            throw new ResourceException("Product Not Found");
        }

        int newCount = product.get().getQuantityInStore() + quantity;

        product.get().setQuantityInStore(newCount);

        Product updatedProductDetails = productRepo.save(product.get());

        return Response.buildResponse("New Stock Added",updatedProductDetails);
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

        productDetails.sort(Comparator.comparing(ProductDetails::getProductId));

        return productDetails;
    }

    public Response deleteProductInformation(String productId) {

        Optional<Product> product = productRepo.findById(productId);

        if(!product.isPresent()){

            throw new ResourceException("Product Not Found");
        }

        productRepo.deleteById(productId);

        String status = String.format("Product '%s' Has Been Deleted Successfully", product.get().getProductName());

        return Response.buildResponse(status,product.get());
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

        productDetails.sort(Comparator.comparing(ProductDetails::getQuantityInStore));

        return productDetails;
    }

    public Response makeProductOutOfStock(String productId) {

        Optional<Product> productInfo = productRepo.findById(productId);

        if(!productInfo.isPresent())
            throw new ResourceException("Product Not Found!!!");

        if(productInfo.get().getQuantityInStore() == 0)
            throw new ResourceException("Product Already Out Of Stock!!!");

        productInfo.get().setQuantityInStore(0);

        return Response.buildResponse("Product Stock Updated",productRepo.save(productInfo.get()));
    }
}

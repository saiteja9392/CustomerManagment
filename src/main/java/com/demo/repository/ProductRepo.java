package com.demo.repository;

import com.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, String> {

    @Query(value = "select * from product where offerkey = ?",nativeQuery = true)
    List<Product> findByOffer(String offerId);
}

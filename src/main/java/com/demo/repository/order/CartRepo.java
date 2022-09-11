package com.demo.repository.order;

import com.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,String> {
    List<Cart> findByLoginId(String loginId);

    Optional<Cart> findByProductId(String productId);
}

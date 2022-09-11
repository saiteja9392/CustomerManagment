package com.demo.repository.order;

import com.demo.entity.order.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepo extends JpaRepository<Wishlist, String> {

    List<Wishlist> findByLoginId(String loginId);
}

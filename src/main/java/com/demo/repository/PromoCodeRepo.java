package com.demo.repository;

import com.demo.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeRepo extends JpaRepository<PromoCode,String> {
}

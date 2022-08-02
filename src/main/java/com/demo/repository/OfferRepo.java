package com.demo.repository;

import com.demo.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepo extends JpaRepository<Offer,String> {
}

package com.demo.repository.support;

import com.demo.entity.support.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepo extends JpaRepository<Support,String> {
    List<Support> findByLoginId(String loginId);
}

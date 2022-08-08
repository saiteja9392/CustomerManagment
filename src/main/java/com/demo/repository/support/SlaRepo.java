package com.demo.repository.support;

import com.demo.entity.support.Sla;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaRepo extends JpaRepository<Sla,Integer> {

    Sla findByType(String priority);
}

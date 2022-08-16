package com.demo.repository.support;

import com.demo.entity.support.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SupportRepo extends JpaRepository<Support,String> {
    List<Support> findByLoginId(String loginId);

    @Query(value = "select * from support where login_id = ?1 and created > ?2 and created < ?3",nativeQuery = true)
    List<Support> getTicketsBasedOnLoginId(String loginId, Date from, Date to);
}

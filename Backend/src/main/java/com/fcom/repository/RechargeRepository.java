package com.fcom.repository;

import com.fcom.model.recharge;
import com.fcom.model.sale;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<recharge, Long> {
    Optional<recharge> findById(Long id);

    List<recharge> findBySellerId(Long id);

    List<recharge> findByDateAndSellerId(String date, Long id);

}

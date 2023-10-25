package com.fcom.repository;

import com.fcom.model.tpe;
import com.fcom.model.recharge;
import com.fcom.model.sale;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TpeRepository extends JpaRepository<tpe, Long> {
    Optional<tpe> findById(Long id);

    List<tpe> findBySellerId(Long id);

    List<tpe> findByDateAndSellerId(String date, Long id);

}

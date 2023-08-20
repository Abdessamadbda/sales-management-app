package com.fcom.repository;


import com.fcom.model.sale;

import com.fcom.model.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<sale, Long> {
	Optional<sale> findById(Long id);
	List<sale> findByDate(String date);
    List<sale> findByDateAndSellerId(String date, Long id);
    List<sale> findBySellerId(Long id);
    void deleteById(Long saleId);
}

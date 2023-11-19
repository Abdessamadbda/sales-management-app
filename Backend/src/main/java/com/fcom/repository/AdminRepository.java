package com.fcom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fcom.model.admin;

@Repository
public interface AdminRepository extends JpaRepository<admin, Long> {
	Optional<admin> findById(Long id);

}

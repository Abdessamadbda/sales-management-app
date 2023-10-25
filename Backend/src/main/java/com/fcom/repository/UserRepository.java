package com.fcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcom.model.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<user, Integer> {
    Optional<user> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<user> findById(Long id);

    List<user> findByVille(String ville);

    List<user> findByAgence(String agence);

}

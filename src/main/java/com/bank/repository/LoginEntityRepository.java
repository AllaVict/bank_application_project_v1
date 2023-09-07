package com.bank.repository;

import com.bank.model.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginEntityRepository extends JpaRepository<LoginEntity, Long> {
    Optional<LoginEntity> findByUsername(String username);

}

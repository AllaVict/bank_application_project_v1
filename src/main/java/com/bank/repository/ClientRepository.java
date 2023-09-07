package com.bank.repository;

import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByPhone(String phone);


}

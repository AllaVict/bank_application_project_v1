package com.bank.repository;



import com.bank.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long>{


}

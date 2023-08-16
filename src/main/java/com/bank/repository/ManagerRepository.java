package com.bank.repository;



import com.bank.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long>{


}

package com.example.employee.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.employee.employee.model.EmployeeCard;

@Repository
public interface CardRepository extends JpaRepository<EmployeeCard, Long> {

        List<EmployeeCard> findByCardNumberContainingIgnoreCase(String cardNumber);

        @Query("SELECT MAX(e.cardNumber) FROM EmployeeCard e")
        String findMaxCardNumber();
}

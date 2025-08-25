package com.example.employee.employee.respository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employee.employee.model.EmployeeCard;

@Repository
public interface CardRespository extends JpaRepository<EmployeeCard, Long> {

        List<EmployeeCard> findByCardNumberContainingIgnoreCase(String cardNumber);

        @Query("SELECT MAX(e.cardNumber) FROM EmployeeCard e")
        String findMaxCardNumber();
}

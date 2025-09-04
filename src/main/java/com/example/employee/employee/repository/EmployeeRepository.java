package com.example.employee.employee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employee.employee.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

        List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName,
                        String lastName);

        List<Employee> findByEmailIdContainingIgnoreCase(String emailId);

        @Query("SELECT e FROM Employee e  WHERE LOWER(e.emailId) LIKE LOWER(CONCAT('%', :keyword, '%')) "
                        + "OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        Page<Employee> searchByKeywordPageWithMetadata(@Param("keyword") String keyword, Pageable pageable);

        // @Query("SELECT e FROM Employee e WHERE LOWER(e.emailId) LIKE
        // LOWER(CONCAT('%', :keyword, '%')) "
        // + "OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        // List<Employee> searchByKeywordPage(@Param("keyword") String keyword, Pageable
        // pageable);

        /**
         * @Query
         * @param keyword
         * @return
         */
        @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
                        + "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
                        + "OR LOWER(e.emailId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Employee> searchEmployees(@Param("keyword") String keyword);

        /**
         * @Query
         * @param email
         * @return
         */
        @Query(value = "SELECT * FROM employees e WHERE LOWER(e.email_id) LIKE LOWER(CONCAT('%', :email, '%'))", nativeQuery = true)
        List<Employee> searchByEmailNative(@Param("email") String email);

        /**
         * @NamedQuery
         * @param email
         * @return
         */
        List<Employee> findByEmail(@Param("email") String email);

        /**
         * @NamedQuery
         * @param email
         * @return
         */
        List<Employee> findByFirstName(@Param("firstName") String firstName);

}

package com.example.employee.employee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employee.employee.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByNameContainingIgnoreCase(String emailId);

    /**
     * @Query
     * @param keyword
     * @return
     */
    @Query("SELECT e FROM Team e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(e.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Team> searchTeams(@Param("keyword") String keyword);

    @Query("SELECT e FROM Team e  WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(e.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Team> searchByKeywordPageWithMetadata(@Param("keyword") String keyword, Pageable pageable);

    // @Query("SELECT e FROM Team e WHERE LOWER(e.name) LIKE
    // LOWER(CONCAT('%', :keyword, '%')) "
    // + "OR LOWER(e.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    // List<Employee> searchByKeywordPage(@Param("keyword") String keyword, Pageable
    // pageable);

}

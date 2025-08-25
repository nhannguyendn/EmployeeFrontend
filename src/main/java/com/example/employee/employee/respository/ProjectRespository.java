package com.example.employee.employee.respository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employee.employee.model.Project;

@Repository
public interface ProjectRespository extends JpaRepository<Project, Long> {

    List<Project> findByNameContainingIgnoreCase(String emailId);

    /**
     * @Query
     * @param keyword
     * @return
     */
    @Query("SELECT e FROM Project e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(e.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Project> searchProjects(@Param("keyword") String keyword);

    @Query("SELECT e FROM Project e  WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(e.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Project> searchByKeywordPageWithMetadata(@Param("keyword") String keyword, Pageable pageable);

    // @Query("SELECT e FROM Project e WHERE LOWER(e.name) LIKE
    // LOWER(CONCAT('%', :keyword, '%')) "
    // + "OR LOWER(e.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    // List<Employee> searchByKeywordPage(@Param("keyword") String keyword, Pageable
    // pageable);

}

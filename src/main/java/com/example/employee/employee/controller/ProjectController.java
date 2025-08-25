package com.example.employee.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.employee.dto.PagedResponse;
import com.example.employee.employee.exception.ResoureNotFoundException;
import com.example.employee.employee.model.Project;
import com.example.employee.employee.respository.ProjectRespository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRespository projectRespository;

    /**
     * Get list Project
     */
    @GetMapping("/projects")
    public List<Project> getAllProject() {
        logger.info("getAllProject");
        return projectRespository.findAll();
    }

    /**
     * Create Project
     */
    @PostMapping("/projects")
    @Transactional
    public Project createEmployee(@RequestBody Project project) {
        return projectRespository.save(project);
    }

    /**
     * Get Project by id
     */
    @GetMapping("/projects/{projectId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        return projectRespository.findById(projectId).map(project -> ResponseEntity.ok(project))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update Project with id
     */
    @PutMapping("/projects/{projectId}")
    @Transactional
    public ResponseEntity<Project> updateProjects(@PathVariable Long projectId,
            @RequestBody Project projectDetails) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found project id=" + projectId));

        project.setName(projectDetails.getName());
        project.setDescriptions(projectDetails.getDescriptions());
        project.setEmployees(projectDetails.getEmployees());
        project.setEmployeeIds(projectDetails.getEmployeeIds());
        Project projectUpdated = projectRespository.save(project);
        //todo: Check table employee to update project 
        return ResponseEntity.ok(projectUpdated);
    }

    /**
     * Delete Project with id
     */

    @DeleteMapping("/projects/{projectId}")
    @Transactional
    public ResponseEntity<Map<String, Boolean>> deleteProject(@PathVariable Long projectId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Project> prOptional = projectRespository.findById(projectId);

        if (prOptional.isPresent()) {
            projectRespository.delete(prOptional.get());
            //todo: Check table employee to delete project 
            result.put("Deleted", true);
        } else {
            result.put("Deleted", false);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Search Project by email (LIKE %email%) of projects
     */
    @GetMapping("/projects/search-name")
    public ResponseEntity<List<Project>> searchNameProject(@RequestParam("name") String name) {
        List<Project> projects = projectRespository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(projects);
    }

    /**
     * Search Project by name (LIKE %name%)
     */
    @GetMapping("/project/keywork")
    public ResponseEntity<List<Project>> searchKeywordEmployee(@RequestParam("keyword") String keyword) {
        List<Project> projects = projectRespository.searchProjects(keyword);
        return ResponseEntity.ok(projects);
    }

    /**
     * Search projects by keyword (email or firstName) + pagination + sort
     */
    @GetMapping("/projects/search-keywork")
    public ResponseEntity<PagedResponse<Project>> searchByKeywordPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("asc")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending());

        // public ResponseEntity<Page<Project>> searchByKeywordPage(
        Page<Project> projects = projectRespository.searchByKeywordPageWithMetadata(keyword, pageable);
        // return ResponseEntity.ok(projects);
        // custom response
        return ResponseEntity.ok(PagedResponse.fromPage(projects));

        // List<Project> projects = projectRespository.searchByKeywordPage(keyword,
        // pageable);
        // return ResponseEntity.ok(projects);
    }
}

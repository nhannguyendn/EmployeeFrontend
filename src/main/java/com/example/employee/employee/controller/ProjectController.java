package com.example.employee.employee.controller;

import java.util.ArrayList;
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
import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.Project;
import com.example.employee.employee.repository.EmployeeRepository;
import com.example.employee.employee.repository.ProjectRepository;
import com.example.employee.exception.ResoureNotFoundException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Get list Project
     */
    @GetMapping("/projects")
    public List<Project> getAllProject() {
        logger.info("getAllProject");
        return projectRepository.findAll();
    }

    /**
     * Create Project
     */
    @PostMapping("/projects")
    @Transactional("employeeTransactionManager")
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    /**
     * Get Project by id
     */
    @GetMapping("/projects/{projectId}")
    @Transactional(transactionManager = "employeeTransactionManager", readOnly = true)
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        return projectRepository.findById(projectId).map(project -> ResponseEntity.ok(project))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update Project with id
     */
    @PutMapping("/projects/{projectId}")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Project> updateProjects(@PathVariable Long projectId,
            @RequestBody Project projectDetails) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found project id=" + projectId));

        project.setName(projectDetails.getName());
        project.setDescriptions(projectDetails.getDescriptions());
        project.setEmployees(projectDetails.getEmployees());
        Project projectUpdated = projectRepository.save(project);
        // todo: Check table employee to update project
        return ResponseEntity.ok(projectUpdated);
    }

    // @PutMapping("/project/add-employees/{projectId}")
    // @Transactional("employeeTransactionManager")
    // public ResponseEntity<Project> addEmployee(
    //         @PathVariable Long projectId,
    //         @RequestBody List<Employee> employees) {

    //     Project project = projectRepository.findById(projectId)
    //             .orElseThrow(() -> new ResoureNotFoundException("Not found projectId id=" + projectId));

    //     for (Employee e : employees) {
    //         boolean projectExists = e.getProjects().stream()
    //                 .anyMatch(p -> p.getId() == project.getId());
    //         if (!projectExists) {
    //             e.getProjects().add(project);
    //         }

    //         boolean employeeExists = project.getEmployees().stream()
    //                 .anyMatch(emp -> emp.getId() == e.getId());
    //         if (!employeeExists) {
    //             project.getEmployees().add(e);
    //         }
    //     }

    //     employeeRepository.saveAll(employees);

    //     return ResponseEntity.ok(project);
    // }

     @PutMapping("/project/add-employees/{projectId}")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Project> addEmployee(
            @PathVariable Long projectId,
            @RequestBody List<Employee> employees) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found projectId id=" + projectId));

        List<Employee> savedEmployees = new ArrayList<>();

        for (Employee e : employees) {
            Employee existing = employeeRepository.findById(e.getId()).orElse(null);

            if (existing != null) {
                existing.setFirstName(e.getFirstName());
                existing.setLastName(e.getLastName());
                existing.setEmailId(e.getEmailId());
            }

            Employee target = existing != null ? existing : e;

            // Thêm project cho employee nếu chưa có
            if (target.getProjects().stream().noneMatch(p -> p.getId() == project.getId())) {
                target.getProjects().add(project);
            }

            // Lưu employee
            target = employeeRepository.save(target);
            savedEmployees.add(target);

            // Thêm employee vào project nếu chưa có
            final Long targetId = e.getId();
            if (project.getEmployees().stream().noneMatch(emp -> emp.getId() == targetId)) {
                project.getEmployees().add(target);
            }
        }

        projectRepository.save(project);

        return ResponseEntity.ok(project);
    }

    /**
     * Delete Project with id
     */

    @DeleteMapping("/projects/{projectId}")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Map<String, Boolean>> deleteProject(@PathVariable Long projectId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Project> prOptional = projectRepository.findById(projectId);

        if (prOptional.isPresent()) {
            projectRepository.delete(prOptional.get());
            // todo: Check table employee to delete project
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
        List<Project> projects = projectRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(projects);
    }

    /**
     * Search Project by name (LIKE %name%)
     */
    @GetMapping("/project/keywork")
    public ResponseEntity<List<Project>> searchKeywordEmployee(@RequestParam("keyword") String keyword) {
        List<Project> projects = projectRepository.searchProjects(keyword);
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
        Page<Project> projects = projectRepository.searchByKeywordPageWithMetadata(keyword, pageable);
        // return ResponseEntity.ok(projects);
        // custom response
        return ResponseEntity.ok(PagedResponse.fromPage(projects));

        // List<Project> projects = projectRepository.searchByKeywordPage(keyword,
        // pageable);
        // return ResponseEntity.ok(projects);
    }
}

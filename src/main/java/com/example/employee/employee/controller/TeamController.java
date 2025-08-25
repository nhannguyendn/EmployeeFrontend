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
import com.example.employee.employee.model.Team;
import com.example.employee.employee.respository.TeamRespository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class TeamController {

    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamRespository teamRespository;

    /**
     * Get list Team
     */
    @GetMapping("/teams")
    public List<Team> getAllTeam() {
        logger.info("getAllTeam");
        return teamRespository.findAll();
    }

    /**
     * Create Team
     */
    @PostMapping("/teams")
    @Transactional
    public Team createEmployee(@RequestBody Team team) {
        return teamRespository.save(team);
    }

    /**
     * Get team by id
     */
    @GetMapping("/teams/{teamId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Team> getTeamById(@PathVariable Long teamId) {
        return teamRespository.findById(teamId).map(team -> ResponseEntity.ok(team))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update team with id
     */
    @PutMapping("/teams/{teamId}")
    @Transactional
    public ResponseEntity<Team> updateTeams(@PathVariable Long teamId,
            @RequestBody Team teamDetails) {
        Team team = teamRespository.findById(teamId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found team id=" + teamId));

        team.setName(teamDetails.getName());
        team.setDescriptions(teamDetails.getDescriptions());
        team.setEmployees(teamDetails.getEmployees());
        Team teamUpdated = teamRespository.save(team);
        //todo: Check table employee to update team 
        return ResponseEntity.ok(teamUpdated);
    }

    /**
     * Delete team with id
     */

    @DeleteMapping("/teams/{teamId}")
    @Transactional
    public ResponseEntity<Map<String, Boolean>> deleteTeam(@PathVariable Long teamId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Team> prOptional = teamRespository.findById(teamId);

        if (prOptional.isPresent()) {
            teamRespository.delete(prOptional.get());
            //todo: Check table employee to delete team 
            result.put("Deleted", true);
        } else {
            result.put("Deleted", false);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Search team by email (LIKE %email%) of teams
     */
    @GetMapping("/teams/search-name")
    public ResponseEntity<List<Team>> searchNameTeam(@RequestParam("name") String name) {
        List<Team> teams = teamRespository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(teams);
    }

    /**
     * Search team by name (LIKE %name%)
     */
    @GetMapping("/team/keywork")
    public ResponseEntity<List<Team>> searchKeywordEmployee(@RequestParam("keyword") String keyword) {
        List<Team> teams = teamRespository.searchTeams(keyword);
        return ResponseEntity.ok(teams);
    }

    /**
     * Search teams by keyword (email or firstName) + pagination + sort
     */
    @GetMapping("/teams/search-keywork")
    public ResponseEntity<PagedResponse<Team>> searchByKeywordPage(
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

        // public ResponseEntity<Page<Team>> searchByKeywordPage(
        Page<Team> teams = teamRespository.searchByKeywordPageWithMetadata(keyword, pageable);
        // return ResponseEntity.ok(teams);
        // custom response
        return ResponseEntity.ok(PagedResponse.fromPage(teams));

        // List<Team> teams = teamRespository.searchByKeywordPage(keyword,
        // pageable);
        // return ResponseEntity.ok(teams);
    }
}

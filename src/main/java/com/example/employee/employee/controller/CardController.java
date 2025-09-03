package com.example.employee.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.employee.employee.dto.EmployeeCardDTO;
import com.example.employee.employee.model.EmployeeCard;
import com.example.employee.employee.repository.CardRepository;
import com.example.employee.exception.ResoureNotFoundException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardRepository cardRepository;

    /**
     * Get list Cards
     */
    @GetMapping("/cards")
    public List<EmployeeCardDTO> getAllCard() {
        logger.info("getAllCard");
        return cardRepository.findAll()
                .stream()
                .map(card -> new EmployeeCardDTO(card.getId(),
                        card.getCardNumber(),
                        card.getEmployee()))
                .toList();
    }

    /**
     * Create EmployeeCard
     */
    @PostMapping("/cards")
    @Transactional("employeeTransactionManager")
    public EmployeeCard createEmployeeCard(@RequestBody EmployeeCard employeeCard) {
        return cardRepository.save(employeeCard);
    }

    /**
     * Update employee on card
     */
    @PutMapping("/cards/{cardId}")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<EmployeeCard> updateEmployeeCards(@PathVariable Long cardId,
            @RequestBody EmployeeCard employeeCardDetails) {
        EmployeeCard employeeCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found cardId id=" + cardId));

        employeeCard.setCardNumber(employeeCardDetails.getCardNumber());
        employeeCard.setEmployee(employeeCardDetails.getEmployee());
        EmployeeCard employeeCardUpdated = cardRepository.save(employeeCard);
        return ResponseEntity.ok(employeeCardUpdated);
    }

    /**
     * Delete card (after delete employee or card empy employee)
     */

    @DeleteMapping("/cards/{cardId}")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Map<String, Boolean>> deleteCard(@PathVariable Long cardId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<EmployeeCard> prOptional = cardRepository.findById(cardId);

        if (prOptional.isPresent()) {
            cardRepository.delete(prOptional.get());
            result.put("Deleted", true);
        } else {
            result.put("Deleted", false);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Search employee by cardNumber (LIKE %cardNumber%) of cards
     */
    @GetMapping("/cards/search-number")
    public ResponseEntity<List<EmployeeCard>> searchEmployee(@RequestParam("cardNumber") String cardNumber) {
        List<EmployeeCard> employeeCards = cardRepository.findByCardNumberContainingIgnoreCase(cardNumber);
        return ResponseEntity.ok(employeeCards);
    }

}

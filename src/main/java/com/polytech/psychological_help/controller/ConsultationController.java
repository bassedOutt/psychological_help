package com.polytech.psychological_help.controller;

import com.polytech.psychological_help.dto.ConsultationDTO;
import com.polytech.psychological_help.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/consultation")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    public ConsultationDTO saveConsultation(@RequestBody ConsultationDTO consultationDTO) {
        return consultationService.createConsultation(consultationDTO);
    }

    @GetMapping("/consultant")
    public List<ConsultationDTO> findUserConsultations() {
        return consultationService.findConsultationsOfCurrentUser();
    }

    @GetMapping
    public List<ConsultationDTO> findAll(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return consultationService.findAllConsultations(date);
    }

    @PutMapping("/{consultationId}")
    public ConsultationDTO bookConsultation(@PathVariable Integer consultationId, @RequestParam String userEmail, @RequestParam String pib) {
        return consultationService.bookConsultation(consultationId, userEmail, pib);
    }

    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Integer consultationId) {
        consultationService.deleteConsultation(consultationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{consultationId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Integer consultationId) {
        consultationService.cancelBooking(consultationId);
        return ResponseEntity.ok().build();
    }
}

package com.polytech.psychological_help.controller;

import com.polytech.psychological_help.dto.ConsultationDTO;
import com.polytech.psychological_help.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public ConsultationDTO saveConsultation(@RequestBody ConsultationDTO consultationDTO) {
        return consultationService.createConsultation(consultationDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ConsultationDTO> findAll(@RequestParam LocalDate date) {
        return consultationService.findAllConsultations(date);
    }

    @PutMapping("/{consultationId}")
    @ResponseStatus(HttpStatus.OK)
    public ConsultationDTO bookConsultation(@PathVariable Integer consultationId, @RequestBody String userData) {
        return consultationService.bookConsultation(consultationId, userData);
    }

    @DeleteMapping("/{consultationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteConsultation(@PathVariable Integer consultationId) {
        consultationService.deleteConsultation(consultationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{consultationId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> cancelBooking(@PathVariable Integer consultationId) {
        consultationService.cancelBooking(consultationId);
        return ResponseEntity.ok().build();
    }
}

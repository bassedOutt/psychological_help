package com.polytech.psychological_help.service;

import com.polytech.psychological_help.dto.ConsultationDTO;

import java.time.LocalDate;
import java.util.List;

public interface ConsultationService {

    List<ConsultationDTO> findAllConsultations(LocalDate date);

    ConsultationDTO createConsultation(ConsultationDTO consultationDTO);

    ConsultationDTO bookConsultation(Integer consultationId, String userEmail, String pib);

    void cancelBooking(Integer consultationId);

    void deleteConsultation(Integer consultationId);
}

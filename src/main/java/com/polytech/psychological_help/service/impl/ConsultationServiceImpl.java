package com.polytech.psychological_help.service.impl;

import com.polytech.psychological_help.dto.ConsultationDTO;
import com.polytech.psychological_help.dto.mapper.ConsultationMapper;
import com.polytech.psychological_help.entity.Consultation;
import com.polytech.psychological_help.entity.User;
import com.polytech.psychological_help.repository.ConsultationRepository;
import com.polytech.psychological_help.service.ConsultationService;
import com.polytech.psychological_help.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final UserService userService;

    private static final ConsultationMapper MAPPER = Mappers.getMapper(ConsultationMapper.class);

    @Override
    public List<ConsultationDTO> findAllConsultations(LocalDate date) {
        return consultationRepository.findAll().stream()
                .map(MAPPER::toConsultationDTO)
                .filter(consultationDto -> date == null || consultationDto.getStartDate().toLocalDate().equals(date))
                .filter(consultationDTO -> consultationDTO.getUserEmail() == null)
                .sorted(Comparator.comparing(ConsultationDTO::getStartDate))
                .collect(Collectors.toList());
    }


    @PreAuthorize(value = "hasRole('CONSULTANT')")
    @Override
    public List<ConsultationDTO> findConsultationsOfCurrentUser() {
        User user = userService.getCurrentUser();
        return consultationRepository.findAll().stream()
                .map(MAPPER::toConsultationDTO)
                .filter(consultationDTO -> consultationDTO.getConsultant().getEmail().equals(user.getEmail()))
                .sorted(Comparator.comparing(ConsultationDTO::getStartDate))
                .collect(Collectors.toList());
    }

    @PreAuthorize(value = "hasRole('CONSULTANT')")
    @Override
    public ConsultationDTO createConsultation(ConsultationDTO consultationDTO) {
        Consultation consultation = MAPPER.fromConsultationDTO(consultationDTO);
        User user = userService.getCurrentUser();
        consultation.setConsultant(user);
        return MAPPER.toConsultationDTO(consultationRepository.save(consultation));
    }

    @Override
    public ConsultationDTO bookConsultation(Integer consultationId, String userEmail, String pib) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow();
        if (consultation.getUserEmail() != null) {
            throw new RuntimeException("This consultation is already booked by another user");
        }
        consultation.setUserEmail(userEmail);
        consultation.setPib(pib);
        return MAPPER.toConsultationDTO(consultationRepository.save(consultation));
    }

    @PreAuthorize(value = "hasRole('CONSULTANT')")
    @Override
    public void cancelBooking(Integer consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation with such id does not exist"));
        consultation.setPib(null);
        consultation.setUserEmail(null);
        consultationRepository.save(consultation);
    }

    @PreAuthorize(value = "hasRole('CONSULTANT')")
    @Override
    public void deleteConsultation(Integer consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation with such id does not exist"));
        consultationRepository.delete(consultation);
    }
}

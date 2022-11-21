package com.polytech.psychological_help.repository;

import com.polytech.psychological_help.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
}

package com.polytech.psychological_help.dto.mapper;

import com.polytech.psychological_help.dto.ConsultationDTO;
import com.polytech.psychological_help.entity.Consultation;
import org.mapstruct.Mapper;

@Mapper
public interface ConsultationMapper {
    ConsultationDTO toConsultationDTO(Consultation consultation);

    Consultation fromConsultationDTO(ConsultationDTO consultation);
}

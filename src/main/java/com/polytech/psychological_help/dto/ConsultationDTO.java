package com.polytech.psychological_help.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationDTO {
    private Integer id;
    private Double price;
    private String userData;
    private UserDTO consultant;
    private String endDate;
    private String startDate;
}

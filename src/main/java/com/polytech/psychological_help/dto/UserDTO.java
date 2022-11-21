package com.polytech.psychological_help.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String email;
}

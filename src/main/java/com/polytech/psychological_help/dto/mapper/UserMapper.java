package com.polytech.psychological_help.dto.mapper;

import com.polytech.psychological_help.dto.UserDTO;
import com.polytech.psychological_help.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toUserDTO(User user);

    User fromUserDTO(UserDTO userDTO);
}

package com.gaurav.vendora.mapper;

import com.gaurav.vendora.modal.User;
import com.gaurav.vendora.payload.dto.UserDto;

public class UserMapper {
    public static UserDto toDTO(User savedUser) {
        
        UserDto userDto = new UserDto();

        userDto.setId(savedUser.getId());
        userDto.setEmail(savedUser.getEmail());
        userDto.setRole(savedUser.getRole());
        userDto.setCreateDateAt(savedUser.getCreateDateAt());
        userDto.setLastlogin(savedUser.getLastlogin());
        userDto.setUpdateDateAt(savedUser.getUpdateDateAt());
        userDto.setPhone(savedUser.getPhone());

        return userDto;
    }
}

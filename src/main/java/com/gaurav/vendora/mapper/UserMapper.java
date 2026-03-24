package com.gaurav.vendora.mapper;

import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.UserDto;

public class UserMapper {
    public static UserDto toDTO(User savedUser) {
        
        UserDto userDto = new UserDto();

        userDto.setId(savedUser.getId());
        userDto.setFullname(savedUser.getFullname());
        userDto.setEmail(savedUser.getEmail());
        //userDto.setPassword(savedUser.getPassword());
        userDto.setRole(savedUser.getRole());
        userDto.setCreateDateAt(savedUser.getCreateDateAt());
        userDto.setLastLogin(savedUser.getLastLogin());
        userDto.setUpdateDateAt(savedUser.getUpdateDateAt());
        userDto.setPhone(savedUser.getPhone());

        return userDto;
    }
}

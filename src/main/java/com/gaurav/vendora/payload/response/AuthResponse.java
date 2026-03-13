package com.gaurav.vendora.payload.response;

import com.gaurav.vendora.payload.dto.UserDto;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserDto user;
}

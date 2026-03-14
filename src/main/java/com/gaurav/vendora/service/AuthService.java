package com.gaurav.vendora.service;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.payload.dto.UserDto;
import com.gaurav.vendora.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse signup(UserDto userDto) throws UserException;
    AuthResponse login(UserDto  userDto) throws UserException;
}

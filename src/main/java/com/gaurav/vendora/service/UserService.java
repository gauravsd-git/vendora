package com.gaurav.vendora.service;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUserFromJwtToken(String token) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(Long id) throws UserException, Exception;
    User createCashier(UserDto userDto) throws UserException;
    List<User> getAllUsers();

    List<UserDto> getCashiersByStore() throws UserException;

    void deleteCashier(Long id) throws UserException;
}

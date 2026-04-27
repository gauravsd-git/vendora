package com.gaurav.vendora.controller;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.UserMapper;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.UserDto;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long id
    ) throws Exception {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PostMapping("/cashier")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<UserDto> createCashier(
            @RequestBody UserDto userDto
    ) throws UserException {
        User cashier = userService.createCashier(userDto);
        return ResponseEntity.ok(UserMapper.toDTO(cashier));
    }
}
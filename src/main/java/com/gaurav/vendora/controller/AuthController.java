package com.gaurav.vendora.controller;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.payload.dto.UserDto;
import com.gaurav.vendora.payload.response.AuthResponse;
import com.gaurav.vendora.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(@RequestBody UserDto userDto)  throws UserException{
       return ResponseEntity.ok(authService.signup(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody UserDto userDto)  throws UserException{
       return ResponseEntity.ok(authService.login(userDto));
    }

}

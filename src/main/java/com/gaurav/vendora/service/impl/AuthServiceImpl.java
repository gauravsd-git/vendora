package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.configurtion.JwtProvider;
import com.gaurav.vendora.domain.UserRole;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.UserMapper;
import com.gaurav.vendora.modal.User;
import com.gaurav.vendora.payload.dto.UserDto;
import com.gaurav.vendora.payload.response.AuthResponse;
import com.gaurav.vendora.repository.UserRepository;
import com.gaurav.vendora.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserImplementation customUserImplementation;

    @Override
    public AuthResponse signup(UserDto userDto) throws UserException {

        User user = userRepository.findByEmail(userDto.getEmail());

        if (user != null) {
            throw new UserException("Email id already registered!");
        }

        if (userDto.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new UserException("Admin already exists!");
        }

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(userDto.getRole());
        newUser.setFullname(userDto.getFullname());
        newUser.setPhone(userDto.getPhone());
        newUser.setLastlogin(LocalDateTime.now());
        newUser.setCreateDateAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        // Load UserDetails for authentication
        UserDetails userDetails =
                customUserImplementation.loadUserByUsername(savedUser.getEmail());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Registered successfully...");
        authResponse.setUser(UserMapper.toDTO(savedUser));

        return authResponse;
    }

    @Override
    public AuthResponse login(UserDto userDto) throws UserException {

        String email = userDto.getEmail();
        String password = userDto.getPassword();

        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        User user = userRepository.findByEmail(email);
        user.setLastlogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successfully...");
        authResponse.setUser(UserMapper.toDTO(user));

        return authResponse;
    }

    private Authentication authenticate(String email, String password) throws UserException {

        UserDetails userDetails;

        try {
            userDetails = customUserImplementation.loadUserByUsername(email);
        } catch (Exception e) {
            throw new UserException(STR."Email id doesn't exist: \{email}");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Password doesn't match!");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
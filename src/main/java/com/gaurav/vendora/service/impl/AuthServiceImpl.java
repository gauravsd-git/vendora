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
        if(user != null){
            throw new UserException("Email id already registered !");
        }
        if(userDto.getRole().equals(UserRole.ROLE_ADMIN)){
            throw new UserException("Admin is already IN !");
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

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                userDto.getEmail(),
                userDto.getPassword()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Registered succesfully...");
        authResponse.setUser(UserMapper.toDTO(savedUser));

        return authResponse;
    }

    @Override
    public AuthResponse login(UserDto userDto) {
        return null;
    }
}

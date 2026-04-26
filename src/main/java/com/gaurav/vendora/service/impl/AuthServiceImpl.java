package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.configuration.JwtProvider;
import com.gaurav.vendora.domain.UserRole;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.UserMapper;
import com.gaurav.vendora.model.Store;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.UserDto;
import com.gaurav.vendora.payload.response.AuthResponse;
import com.gaurav.vendora.repository.StoreRepository;
import com.gaurav.vendora.repository.UserRepository;
import com.gaurav.vendora.service.AuthService;

import jakarta.transaction.Transactional;
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
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserImplementation customUserImplementation;

    @Transactional
    @Override
    public AuthResponse signup(UserDto userDto) throws UserException {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserException("Email already registered!");
        }

        if (userDto.getRole() != UserRole.ROLE_STORE_ADMIN) {
            throw new UserException("Only Store Admin can register");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setFullname(userDto.getFullname());
        user.setPhone(userDto.getPhone());
        user.setCreateDateAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Store store = new Store();
        store.setBrand(savedUser.getFullname() + "'s Store");
        store.setStoreAdmin(savedUser);

        Store savedStore = storeRepository.save(store);

        savedUser.setStore(savedStore);
        userRepository.save(savedUser);

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

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Registered successfully...");
        response.setUser(UserMapper.toDTO(savedUser));

        return response;
    }

    @Override
    public AuthResponse login(UserDto userDto) throws UserException {

        String email = userDto.getEmail();
        String password = userDto.getPassword();

        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        User user = userRepository.findByEmail(email);

        if (user.getStore() == null) {
            throw new UserException("User not assigned to any store");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Login successfully...");
        response.setUser(UserMapper.toDTO(user));

        return response;
    }

    private Authentication authenticate(String email, String password) throws UserException {

        UserDetails userDetails;

        try {
            userDetails = customUserImplementation.loadUserByUsername(email);
        } catch (Exception e) {
            throw new UserException("Email does not exist: " + email);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Invalid password!");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
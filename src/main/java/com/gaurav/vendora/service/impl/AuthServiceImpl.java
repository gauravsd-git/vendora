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

    // =====================================================
    // STORE OWNER SELF REGISTRATION
    // Only STORE_ADMIN can signup here
    // =====================================================
    @Transactional
    @Override
    public AuthResponse signup(UserDto userDto) throws UserException {

        validateNewUser(userDto);

        if (userDto.getRole() != UserRole.STORE_ADMIN) {
            throw new UserException("Only Store Admin can register");
        }

        User savedUser = createUser(userDto);

        // Auto create store for owner
        Store store = new Store();
        store.setBrand(savedUser.getFullname() + "'s Store");
        store.setStoreAdmin(savedUser);

        Store savedStore = storeRepository.save(store);

        savedUser.setStore(savedStore);
        userRepository.save(savedUser);

        return buildAuthResponse(savedUser, "Store Admin registered successfully...");
    }

    // =====================================================
    // LOGIN FOR ALL ROLES
    // ADMIN / STORE_ADMIN / CASHIER
    // =====================================================
    @Override
    public AuthResponse login(UserDto userDto) throws UserException {

        String email = userDto.getEmail();
        String password = userDto.getPassword();

        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Login successful...");
        response.setUser(UserMapper.toDTO(user));

        return response;
    }

    // =====================================================
    // STORE ADMIN CREATES CASHIER
    // Call from UserService / Admin Panel
    // =====================================================
    @Transactional
    public User createCashier(UserDto userDto, Store store) throws UserException {

        validateNewUser(userDto);

        if (userDto.getRole() != UserRole.CASHIER) {
            throw new UserException("Only CASHIER role allowed here");
        }

        User cashier = createUser(userDto);
        cashier.setStore(store);

        return userRepository.save(cashier);
    }

    // =====================================================
    // COMMON USER CREATION
    // =====================================================
    private User createUser(UserDto userDto) {

        User user = new User();

        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());

        user.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        user.setRole(userDto.getRole());

        user.setCreateDateAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        return userRepository.save(user);
    }

    // =====================================================
    // VALIDATIONS
    // =====================================================
    private void validateNewUser(UserDto userDto) throws UserException {

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new UserException("Email is required");
        }

        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new UserException("Password is required");
        }

        if (userDto.getRole() == null) {
            throw new UserException("Role is required");
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserException("Email already registered");
        }
    }

    // =====================================================
    // SPRING AUTHENTICATION
    // =====================================================
    private Authentication authenticate(String email, String password)
            throws UserException {

        UserDetails userDetails;

        try {
            userDetails =
                    customUserImplementation.loadUserByUsername(email);

        } catch (Exception e) {
            throw new UserException("Email does not exist");
        }

        if (!passwordEncoder.matches(
                password,
                userDetails.getPassword()
        )) {
            throw new UserException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    // =====================================================
    // JWT RESPONSE
    // =====================================================
    private AuthResponse buildAuthResponse(User user, String msg) {

        UserDetails userDetails =
                customUserImplementation.loadUserByUsername(user.getEmail());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String jwt =
                jwtProvider.generateJwtToken(authentication);

        AuthResponse response = new AuthResponse();

        response.setJwt(jwt);
        response.setMessage(msg);
        response.setUser(UserMapper.toDTO(user));

        return response;
    }
}
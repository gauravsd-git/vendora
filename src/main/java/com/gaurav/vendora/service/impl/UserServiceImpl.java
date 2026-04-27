package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.configuration.JwtProvider;
import com.gaurav.vendora.domain.UserRole;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.UserDto;
import com.gaurav.vendora.repository.StoreRepository;
import com.gaurav.vendora.repository.UserRepository;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserFromJwtToken(String token) throws UserException {

        String email = jwtProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserException("Invalid token");
        }
        return user;
    }

    @Override
    public User getCurrentUser() throws UserException {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user =userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User Not Found!");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user =userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User Not Found!");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(
                ()-> new Exception("User Not Found")
        ) ;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createCashier(UserDto userDto) throws UserException {

        User currentUser = getCurrentUser();

        if (currentUser.getRole() != UserRole.STORE_ADMIN) {
            throw new UserException("Only Store Admin can create cashier");
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserException("Email already registered");
        }

        User cashier = new User();
        cashier.setFullname(userDto.getFullname());
        cashier.setEmail(userDto.getEmail());
        cashier.setPhone(userDto.getPhone());
        cashier.setPassword(passwordEncoder.encode(userDto.getPassword()));
        cashier.setRole(UserRole.CASHIER);
        cashier.setStore(currentUser.getStore());
        cashier.setCreateDateAt(LocalDateTime.now());
        cashier.setLastLogin(LocalDateTime.now());

        return userRepository.save(cashier);
    }
}

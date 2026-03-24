package com.gaurav.vendora.payload.dto;

import com.gaurav.vendora.domain.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private long id;
    private String fullname;
    private String email;
    private String phone;
    private UserRole role;
    private String password;
    private LocalDateTime createDateAt;
    private LocalDateTime updateDateAt;
    private LocalDateTime lastLogin;
}

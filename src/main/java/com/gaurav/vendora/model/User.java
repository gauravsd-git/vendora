package com.gaurav.vendora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gaurav.vendora.domain.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be vailid")
    private String email;

    private String phone;

    @Column(nullable = false)
    private UserRole role;

    @JsonProperty(access =
    JsonProperty.Access.WRITE_ONLY)
    private String password;

    private LocalDateTime createDateAt;
    private LocalDateTime updateDateAt;
    private LocalDateTime lastLogin;
}

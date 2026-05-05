package com.gaurav.vendora.model;

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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private LocalDateTime createDateAt;
    private LocalDateTime updateDateAt;
    private LocalDateTime lastLogin;
}
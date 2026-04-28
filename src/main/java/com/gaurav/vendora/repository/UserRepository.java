package com.gaurav.vendora.repository;

import com.gaurav.vendora.domain.UserRole;
import com.gaurav.vendora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    List<User> findByStoreIdAndRole(Long storeId, UserRole role);
}

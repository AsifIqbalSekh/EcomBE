package com.asifiqbalsekh.EcomBE.repository;

import com.asifiqbalsekh.EcomBE.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Optional<User> findByEmail( String email);
}

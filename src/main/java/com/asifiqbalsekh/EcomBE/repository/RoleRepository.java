package com.asifiqbalsekh.EcomBE.repository;

import com.asifiqbalsekh.EcomBE.dto.AppRole;
import com.asifiqbalsekh.EcomBE.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}

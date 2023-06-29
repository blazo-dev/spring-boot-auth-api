package dev.blazo.crud.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.blazo.crud.security.entity.Role;
import dev.blazo.crud.security.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}

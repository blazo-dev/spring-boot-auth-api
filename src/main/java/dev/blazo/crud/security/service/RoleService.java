package dev.blazo.crud.security.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.blazo.crud.security.entity.Role;
import dev.blazo.crud.security.enums.RoleName;
import dev.blazo.crud.security.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public void save(Role role) {
        roleRepository.save(role);
    }
}

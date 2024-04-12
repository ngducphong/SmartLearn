package com.example.elearning.service.impl;


import com.example.elearning.constant.RoleName;
import com.example.elearning.dto.RoleDto;
import com.example.elearning.model.Roles;
import com.example.elearning.repository.RoleRepository;
import com.example.elearning.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Roles findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new RuntimeException("role not found"));
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream().map(RoleDto::new).collect(Collectors.toList());
    }
}

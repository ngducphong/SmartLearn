package com.example.elearning.service;

import com.example.elearning.constant.RoleName;
import com.example.elearning.dto.RoleDto;
import com.example.elearning.model.Roles;


import java.util.List;

public interface RoleService {
    Roles findByRoleName(RoleName roleName);

    List<RoleDto> getAll();
}

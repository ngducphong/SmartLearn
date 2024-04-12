package com.example.elearning.dto;

import com.example.elearning.constant.RoleName;
import com.example.elearning.model.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private RoleName roleName;
    public RoleDto(Roles roles){
        this.roleName = roles.getRoleName();
    }
}

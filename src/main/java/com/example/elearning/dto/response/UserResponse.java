package com.example.elearning.dto.response;

import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponse extends BaseObjectDto {

    private String username;
    private String fullName;
    private String phone;
    private String email;
    private List<String> role;
    private Boolean voided;

    public UserResponse() {
    }

    public UserResponse(Users users) {
        setId(users.getId());
        this.setCreateDate(users.getCreateDate());
        this.username = users.getUsername();
        this.fullName = users.getFullName();
        this.phone = users.getPhone();
        this.email = users.getEmail();
        this.voided = users.getVoided();
        role = new ArrayList<>();
        if(users.getRoles() != null){
            users.getRoles().forEach(e->role.add(e.getRoleName().name()));
        }
    }
}

package com.example.elearning.dto;

import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersDto extends BaseObjectDto {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String password;

    public UsersDto() {
    }

    public UsersDto(Users entity) {
        this.username = entity.getUsername();
        this.fullName = entity.getFullName();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
//        this.password = entity.getPassword();
        this.voided = entity.getVoided();
    }

    public UsersDto(Users entity, Boolean isGetFull) {
        this.username = entity.getUsername();
        this.fullName = entity.getFullName();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
//        this.password = entity.getPassword();
        this.voided = entity.getVoided();

        if (isGetFull) {
            this.createDate = entity.getCreateDate();
            this.modifyBy = entity.getModifyBy();
            this.createBy = entity.getCreateBy();
            this.modifyDate = entity.getModifyDate();
        }
    }
}

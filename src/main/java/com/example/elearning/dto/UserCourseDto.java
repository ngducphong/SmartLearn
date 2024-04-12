package com.example.elearning.dto;

import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.UserCourse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCourseDto extends BaseObjectDto {
    private UsersDto users;
    private CourseDto courseDto;
    private Boolean isFavourite;

    public UserCourseDto() {
    }

    public UserCourseDto(UserCourse entity, Boolean isGetFull ) {
        this.id = entity.getId();
        this.isFavourite = entity.getIsFavourite();
        if(entity.getUsers() != null && entity.getUsers().getId() != null){
            this.users = new UsersDto(entity.getUsers());
        }
        if(entity.getCourse() != null && entity.getCourse().getId() != null){
            this.courseDto = new CourseDto(entity.getCourse());
        }
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
        if (isGetFull) {
            this.createDate = entity.getCreateDate();
            this.modifyBy = entity.getModifyBy();
            this.createBy = entity.getCreateBy();
            this.modifyDate = entity.getModifyDate();
        }
    }

    public UserCourseDto(UserCourse entity ) {
        this.id = entity.getId();
        this.isFavourite = entity.getIsFavourite();
        if(entity.getUsers() != null && entity.getUsers().getId() != null){
            this.users = new UsersDto(entity.getUsers());
        }
        if(entity.getCourse() != null && entity.getCourse().getId() != null){
            this.courseDto = new CourseDto(entity.getCourse());
        }
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
    }
}

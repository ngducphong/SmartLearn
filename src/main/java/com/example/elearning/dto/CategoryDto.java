package com.example.elearning.dto;
import com.example.elearning.dto.base.BaseObjectDto;

import com.example.elearning.model.Category;
import com.example.elearning.model.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryDto extends BaseObjectDto {
    private String name;
    private String description;
    private List<CourseDto> courses = new ArrayList<>();

    public CategoryDto() {
    }
    public CategoryDto(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();

        if(entity.getCourses() != null && !entity.getCourses().isEmpty()){
            this.courses = new ArrayList<>();
            for (Course course : entity.getCourses()){
                CourseDto courseDto = new CourseDto(course, true);
                this.courses.add(courseDto);
            }
        }
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
    }
    public CategoryDto(Category entity, Boolean isFull) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        if(entity.getCourses() != null && !entity.getCourses().isEmpty()){
            this.courses = new ArrayList<>();
            for (Course course : entity.getCourses()){
                CourseDto courseDto = new CourseDto(course, true);
                this.courses.add(courseDto);
            }
        }
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
        if(isFull){
            this.createDate = entity.getCreateDate();
            this.modifyBy = entity.getModifyBy();
            this.createBy = entity.getCreateBy();
            this.modifyDate = entity.getModifyDate();
        }
    }
}

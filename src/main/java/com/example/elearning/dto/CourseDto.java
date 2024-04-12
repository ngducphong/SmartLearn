package com.example.elearning.dto;


import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.Course;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CourseDto extends BaseObjectDto {
    private String title;
    private String image;
    private Double price;
    private String description;
    private MultipartFile imageFile;
    private String subDescription;

    public CourseDto() {
    }

    public CourseDto(Course entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.image = entity.getImage();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.subDescription = entity.getSubDescription();
        if (entity.getVoided() != null) {
            this.voided = entity.getVoided();
        }
    }

    public CourseDto(Course entity, Boolean isGetFull) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.image = entity.getImage();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.subDescription = entity.getSubDescription();
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
}

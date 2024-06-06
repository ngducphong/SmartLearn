package com.example.elearning.dto;


import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.Category;
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
    private Long totalChapter;
    private Long totalFavourite;
    private Long totalUser;
    private Boolean isRegister;
    private Boolean isFavourite;
    private CategoryDto category;
    private Long categoryId;

    public CourseDto() {
    }

    public CourseDto(Course entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.image = entity.getImage();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.subDescription = entity.getSubDescription();
        if(entity.getCategory() != null){
//            this.category = new CategoryDto(entity.getCategory());
            this.categoryId = entity.getCategory().getId();
        }
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
        if(entity.getCategory() != null){
//            this.category = new CategoryDto(entity.getCategory());
            this.categoryId = entity.getCategory().getId();
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

    public CourseDto(Course entity, Boolean isGetFull, Boolean isFavourite) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.image = entity.getImage();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.subDescription = entity.getSubDescription();
        this.isFavourite = isFavourite;
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

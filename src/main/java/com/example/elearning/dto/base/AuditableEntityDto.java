package com.example.elearning.dto.base;

import com.example.elearning.model.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class AuditableEntityDto {
    protected Date createDate;
    protected Date modifyDate;
    protected String createBy;
    protected String modifyBy;

    public AuditableEntityDto() {
    }

    public AuditableEntityDto(AuditableEntity entity) {
        if (entity != null) {
            this.createDate = entity.getCreateDate();
            this.createBy = entity.getCreateBy();
            this.modifyDate = entity.getModifyDate();
            this.modifyBy = entity.getModifyBy();
        }

    }

    public AuditableEntityDto(Date createDate, Date modifyDate, String createBy, String modifyBy) {
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.createBy = createBy;
        this.modifyBy = modifyBy;
    }
}

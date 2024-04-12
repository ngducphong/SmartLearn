package com.example.elearning.dto.base;

import com.example.elearning.model.base.BaseObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseObjectDto extends AuditableEntityDto{
    protected Long id;
    protected boolean voided;

    public BaseObjectDto() {
    }
    public BaseObjectDto(BaseObject entity) {
        super(entity);
        if (entity != null) {
            this.id = entity.getId();
            if (entity.getVoided() != null) {
                this.voided = entity.getVoided();
            }
        }
    }
}

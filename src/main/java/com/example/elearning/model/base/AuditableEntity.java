package com.example.elearning.model.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@MappedSuperclass
@EntityListeners(EntityAuditListener.class)
@Setter
@Getter
public class AuditableEntity {
	private Date createDate;
	private Date modifyDate;
	private String createBy;
	private String modifyBy;
}

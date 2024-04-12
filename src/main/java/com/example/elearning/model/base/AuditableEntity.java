package com.example.elearning.model.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(EntityAuditListener.class)
@Setter
@Getter
public class AuditableEntity {
	private LocalDate createDate;
	private LocalDate modifyDate;
	private String createBy;
	private String modifyBy;
}

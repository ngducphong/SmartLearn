package com.example.elearning.model.base;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDate;
import java.util.Date;

public class EntityAuditListener {
	@PrePersist
	public void beforePersist(AuditableEntity auditableEntity) {
		Date date = new Date();
		auditableEntity.setCreateDate(date);
		auditableEntity.setModifyDate(date);
		
	}
	
	@PreUpdate
	public void beforeMerge(AuditableEntity auditableEntity) {
		Date date = new Date();
		auditableEntity.setModifyDate(date);
	}
}

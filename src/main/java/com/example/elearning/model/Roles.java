package com.example.elearning.model;


import com.example.elearning.constant.RoleName;
import com.example.elearning.model.base.BaseObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Roles extends BaseObject {
	@Column(unique = true)
	private RoleName roleName;
}

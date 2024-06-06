package com.example.elearning.model;

import com.example.elearning.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category extends BaseObject {
	private String name;
	@Column(columnDefinition="LONGTEXT")
	private String description;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Course> courses = new HashSet<>();
}

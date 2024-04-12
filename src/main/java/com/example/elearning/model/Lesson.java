package com.example.elearning.model;

import com.example.elearning.model.base.BaseObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Lesson extends BaseObject {
	private String title;
	private String video;
	private String resources;
	@Column(columnDefinition="LONGTEXT")
	private String description;
	private String document;
	@ManyToOne
	@JoinColumn(name = "chapter_id")
	private Chapter chapter;
}

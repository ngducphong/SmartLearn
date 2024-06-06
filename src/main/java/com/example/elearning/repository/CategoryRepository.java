package com.example.elearning.repository;
import com.example.elearning.dto.CategoryDto;
import com.example.elearning.dto.LessonDto;
import com.example.elearning.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select new com.example.elearning.dto.CategoryDto(e, true) from Category e where (e.voided is null or e.voided = false)")
    List<CategoryDto> getAll();

    @Query("select new com.example.elearning.dto.CategoryDto(e, true) from Category e"
            + " Where  ( :name is null or  e.name like concat('%',:name,'%'))")
    Page<CategoryDto> getCategoryPage(Pageable pageable, String name);
}

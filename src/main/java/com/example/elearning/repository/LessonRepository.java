package com.example.elearning.repository;


import com.example.elearning.dto.LessonDto;
import com.example.elearning.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    @Query("select new com.example.elearning.dto.LessonDto(e, true) from Lesson e  ")
    List<LessonDto> getAll();

    @Query("select new com.example.elearning.dto.LessonDto(e, true) from Lesson e"
            + " Where  ( :title is null or  e.title like concat('%',:title,'%'))")
    Page<LessonDto> getLessonPage(Pageable pageable, String title);

    @Query("select count(e) from Lesson e "
            + "  Where  e.chapter.course.id = :courseId")
    Long countLessonByCourseId(Long courseId);
}

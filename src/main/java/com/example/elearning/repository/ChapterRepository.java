package com.example.elearning.repository;


import com.example.elearning.dto.ChapterDto;
import com.example.elearning.model.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    @Query("select new com.example.elearning.dto.ChapterDto(e, true) from Chapter e ")
    List<ChapterDto> getAll();

    @Query("select new com.example.elearning.dto.ChapterDto(e, true) from Chapter e"
            + " Where ( :title is null or  e.title like concat('%',:title,'%'))")
    Page<ChapterDto> getChapterPage(Pageable pageable, String title);

    @Query("select new com.example.elearning.dto.ChapterDto(e, true) from Chapter e"
            + " Where ( e.course.id = :courseId )")
    List<ChapterDto> getChaptersByCourseId(Long courseId);

    @Query("select count(e) from Chapter e "
            + "  Where  e.course.id = :courseId")
    Long countChapterByCourseId(Long courseId);
}

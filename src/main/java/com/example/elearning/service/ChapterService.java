package com.example.elearning.service;


import com.example.elearning.dto.ChapterDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChapterService {

    ChapterDto saveChapter(ChapterDto dto) throws CustomException;
    ChapterDto upDateChapter(ChapterDto dto, Long id) throws CustomException;

    void deleteChapter(Long id) throws CustomException;

    List<ChapterDto> getAllChapter();

    ChapterDto getChapterDtoById(Long id) throws CustomException;

    Page<ChapterDto> pagingChapterDto(Pageable pageable, String title);

    List<ChapterDto> getChaptersByCourseId(Long courseId);
}

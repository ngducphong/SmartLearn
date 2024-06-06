package com.example.elearning.service;


import com.example.elearning.dto.LessonDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LessonService {

    LessonDto saveLesson(LessonDto dto) throws CustomException;
    LessonDto upDateLesson(LessonDto dto, Long id) throws CustomException;

    void deleteLesson(Long id) throws CustomException;

    List<LessonDto> getAllLesson();

    LessonDto getLessonDtoById(Long id) throws CustomException;

    Page<LessonDto> pagingLessonDto(Pageable pageable, String title);
}

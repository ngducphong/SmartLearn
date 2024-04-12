package com.example.elearning.service;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    CourseDto saveCourse(CourseDto dto) throws IOException;
    CourseDto upDateCourse(CourseDto dto, Long id) throws CustomException, IOException;

    void deleteCourse(Long id) throws CustomException;

    List<CourseDto> getAllCourse();

    CourseDto getCourseDtoById(Long id) throws CustomException;

    Page<CourseDto> pagingCourseDto(Pageable pageable, String title,String home);



}

package com.example.elearning.service;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CourseService {

    CourseDto saveCourse(CourseDto dto) throws IOException, CustomException;

    CourseDto upDateCourse(CourseDto dto, Long id) throws CustomException, IOException;

    void deleteCourse(Long id) throws CustomException;

    List<CourseDto> getAllCourse();

    CourseDto getCourseDtoById(Long id) throws CustomException;
    CourseDto getFullCourse(Long id) throws CustomException;

    Page<CourseDto> pagingCourseDto(Pageable pageable, String title, String home, String price, String createDate, Boolean voided, List<Long> courseIds) throws ParseException;

    Page<CourseDto> pagingCourseMostRegistered(Pageable pageable);

    Page<CourseDto> pagingCourseFavourite(Pageable pageable);

    Page<CourseDto> getAllMyCourseDto(Pageable pageable, String title, List<Long> courseIds, String price) throws CustomException;
    Boolean checkRegisterCourse(Long courseId);
    List<CourseDto> recommendCourseDtoById(Long id, Long number);
    List<CourseDto> recommendCourseByMyCourse() throws CustomException;

}

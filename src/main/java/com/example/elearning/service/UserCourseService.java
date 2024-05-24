package com.example.elearning.service;


import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface UserCourseService {

    UserCourseDto saveUserCourse(UserCourseDto dto) throws CustomException;
    UserCourseDto saveUserCourseByUsernameAndCourseId(Long courseId, String username) throws CustomException;
    UserCourseDto upDateUserCourse(UserCourseDto dto, Long id) throws CustomException, IOException;
    Boolean favourite(Long id) throws CustomException, IOException;

    void deleteUserCourse(Long id) throws CustomException;

    List<UserCourseDto> getAllUserCourse();

    UserCourseDto getUserCourseDtoById(Long id) throws CustomException;



}

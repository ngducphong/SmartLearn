package com.example.elearning.service.impl;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Course;
import com.example.elearning.model.UserCourse;
import com.example.elearning.model.Users;
import com.example.elearning.repository.CourseRepository;
import com.example.elearning.repository.UserCourseRepository;
import com.example.elearning.repository.UserRepository;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.UserCourseService;
import com.example.elearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    UserCourseRepository userCourseRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    UserRepository userRepository;

    public UserCourseDto save(UserCourse entity, UserCourseDto dto, Boolean isUpdate) throws CustomException {
        entity.setVoided(dto.isVoided());

        if (dto.getCourseDto() == null || dto.getCourseDto().getId() == null) {
            throw new CustomException("Course is not null");
        }
        Course course = courseRepository.findById(dto.getCourseDto().getId()).orElse(null);
        if (course == null) {
            throw new CustomException("Course not found");
        }
        entity.setCourse(course);
        if (!isUpdate && userCourseRepository.getByUsersIdAndCourseId(entity.getUsers().getId(), course.getId()).size() > 0){
            throw new CustomException("UserCourse exist");
        }
        entity = userCourseRepository.save(entity);
        return new UserCourseDto(entity);
    }


    @Override
    public UserCourseDto saveUserCourse(UserCourseDto dto) throws CustomException {
        UserCourse userCourse = new UserCourse();

        Users users = userService.getCurrentUser();
        userCourse.setUsers(users);

        return this.save(userCourse, dto, false);
    }
    @Override
    public UserCourseDto saveUserCourseByUsernameAndCourseId(Long courseId, String username) throws CustomException {
        UserCourse userCourse = new UserCourse();
        UserCourseDto dto = new UserCourseDto();

        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        dto.setCourseDto(courseDto);

        Users users = userRepository.findUsersByUsername(username).orElseThrow(() -> new CustomException("UserCourse not found"));
        userCourse.setUsers(users);

        return this.save(userCourse, dto, false);
    }

    @Override
    public UserCourseDto upDateUserCourse(UserCourseDto dto, Long id) throws CustomException, IOException {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new CustomException("UserCourse not found"));
        return this.save(userCourse, dto, true);
    }

    @Override
    public Boolean favourite(Long id) throws CustomException, IOException {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new CustomException("UserCourse not found"));
        if (userCourse.getIsFavourite() == null || userCourse.getIsFavourite() == false) {
            userCourse.setIsFavourite(true);
        } else {
            userCourse.setIsFavourite(false);
        }
        userCourse = userCourseRepository.save(userCourse);
        return userCourse.getIsFavourite();
    }


    @Override
    public void deleteUserCourse(Long id) throws CustomException {
        UserCourse userCourse = userCourseRepository.findById(id).orElseThrow(() -> new CustomException("UserCourse not found"));
        if (userCourse.getVoided() == null || userCourse.getVoided() == false) {
            userCourse.setVoided(true);
        } else {
            userCourse.setVoided(false);
        }
        userCourseRepository.save(userCourse);
    }

    @Override
    public List<UserCourseDto> getAllUserCourse() {
        return userCourseRepository.getAll();
    }

    @Override
    public UserCourseDto getUserCourseDtoById(Long id) throws CustomException {
        return new UserCourseDto(this.getUserCourseById(id));
    }

    private UserCourse getUserCourseById(Long id) throws CustomException {
        Optional<UserCourse> optional = userCourseRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CustomException("UserCourse not found");
    }


}

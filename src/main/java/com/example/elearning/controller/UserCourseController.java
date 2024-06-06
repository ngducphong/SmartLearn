package com.example.elearning.controller;


import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/user-course")
@RestController
public class UserCourseController {

    @Autowired
    private UserCourseService userCourseService;

//    @Secured("ROLE_ADMIN")
//    @PostMapping("/save")
//    public ResponseEntity<UserCourseDto> save(@RequestBody UserCourseDto request) throws IOException, CustomException {
//        UserCourseDto ret = userCourseService.saveUserCourse(request);
//        return ResponseEntity.ok(ret);
//    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserCourseDto> update(@RequestBody UserCourseDto request, @PathVariable Long id) throws CustomException, IOException {
        UserCourseDto ret = userCourseService.upDateUserCourse(request, id);
        return ResponseEntity.ok(ret);
    }

    @PutMapping("/favourite/{courseId}")
    public ResponseEntity<Boolean> favourite(@PathVariable Long courseId) throws CustomException, IOException {

        Boolean ret = userCourseService.favourite(courseId);
        return ResponseEntity.ok(ret);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws CustomException {
        userCourseService.deleteUserCourse(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserCourseDto>> getAll() {
        List<UserCourseDto> ret = userCourseService.getAllUserCourse();
        return ResponseEntity.ok(ret);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserCourseDto> get(@PathVariable("id") Long id) throws CustomException {
        UserCourseDto ret = userCourseService.getUserCourseDtoById(id);
        return ResponseEntity.ok(ret);
    }


}

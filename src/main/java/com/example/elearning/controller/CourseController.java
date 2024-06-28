package com.example.elearning.controller;


import com.example.elearning.dto.CourseDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/course")
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;


    @Secured({"ROLE_ADMIN"})
    @PostMapping("/save")
    public ResponseEntity<CourseDto> save(@ModelAttribute CourseDto request) throws IOException, CustomException {
        CourseDto ret = courseService.saveCourse(request);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<CourseDto> update(@ModelAttribute CourseDto request, @PathVariable Long id) throws CustomException, IOException {
        CourseDto ret = courseService.upDateCourse(request, id);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws CustomException {
        courseService.deleteCourse(id);
    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/get-all")
    public ResponseEntity<List<CourseDto>> getAll() {
        List<CourseDto> ret = courseService.getAllCourse();
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<CourseDto>> paging(@RequestParam(required = false)String home,@PageableDefault(page = 0, size = 20,sort = "id",direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(required = false) String title) {
        Page<CourseDto> ret = courseService.pagingCourseDto(pageable, title, home);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> get(@PathVariable("id") Long id) throws CustomException {
        CourseDto ret = courseService.getCourseDtoById(id);
        return ResponseEntity.ok(ret);

    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/getFullCourse/{id}")
    public ResponseEntity<CourseDto> getFullCourse(@PathVariable("id") Long id) throws CustomException {
        CourseDto ret = courseService.getFullCourse(id);
        return ResponseEntity.ok(ret);

    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/get-my-course")
    public ResponseEntity<Page<CourseDto>> getAllMyCourseDto(@PageableDefault(page = 0, size = 20) Pageable pageable, @RequestParam(required = false) String title) throws CustomException {
        Page<CourseDto> ret = courseService.getAllMyCourseDto(pageable, title);
        return ResponseEntity.ok(ret);
    }
    @GetMapping("/paging-course-most-registered")
    public ResponseEntity<Page<CourseDto>> pagingCourseMostRegistered(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<CourseDto> ret = courseService.pagingCourseMostRegistered(pageable);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/paging-course-favourite")
    public ResponseEntity<Page<CourseDto>> pagingCourseFavourite(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<CourseDto> ret = courseService.pagingCourseFavourite(pageable);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/check-register-course/{id}")
    public ResponseEntity<Boolean> checkRegisterCourse(@PathVariable("id") Long id) {
        Boolean ret = courseService.checkRegisterCourse(id);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/recommend-course-by-id")
    public ResponseEntity<List<CourseDto>> recommendCourseDtoById(@RequestParam(required = false) Long id, @RequestParam(required = false, defaultValue = "6") Long number) {
        List<CourseDto> ret = courseService.recommendCourseDtoById(id, number);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/recommend-course-by-my-course")
    public ResponseEntity<List<CourseDto>> recommendCourseByMyCourse() throws CustomException {
        List<CourseDto> ret = courseService.recommendCourseByMyCourse();
        return ResponseEntity.ok(ret);
    }
}

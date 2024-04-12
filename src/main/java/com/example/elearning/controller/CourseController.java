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
    public ResponseEntity<CourseDto> save(@ModelAttribute CourseDto request) throws IOException {
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


}

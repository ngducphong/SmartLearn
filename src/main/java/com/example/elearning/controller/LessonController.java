package com.example.elearning.controller;


import com.example.elearning.dto.LessonDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/lesson")
@RestController
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/save")
    public ResponseEntity<LessonDto> save(@ModelAttribute LessonDto request) throws CustomException {
        LessonDto ret = lessonService.saveLesson(request);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<LessonDto> update(@ModelAttribute LessonDto request, @PathVariable Long id) throws CustomException {
        LessonDto ret = lessonService.upDateLesson(request, id);
        return ResponseEntity.ok(ret);
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws CustomException {
        lessonService.deleteLesson(id);
    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/get-all")
    public ResponseEntity<List<LessonDto>> getAll() {
        List<LessonDto> ret = lessonService.getAllLesson();
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/paging")
    public ResponseEntity<Page<LessonDto>> paging(@PageableDefault(page = 0, size = 2,sort = "id",direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(required = false) String title) {
        Page<LessonDto> ret = lessonService.pagingLessonDto(pageable, title);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_SUBADMIN","ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> get(@PathVariable("id") Long id) throws CustomException {
        LessonDto ret = lessonService.getLessonDtoById(id);
        return ResponseEntity.ok(ret);
    }

}

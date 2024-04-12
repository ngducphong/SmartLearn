package com.example.elearning.controller;


import com.example.elearning.dto.ChapterDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/chapter")
@RestController
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/save")
    public ResponseEntity<ChapterDto> save(@RequestBody ChapterDto request) throws CustomException {
        ChapterDto ret = chapterService.saveChapter(request);
        return ResponseEntity.ok(ret);
    }
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<ChapterDto> update(@RequestBody ChapterDto request, @PathVariable Long id) throws CustomException {
        ChapterDto ret = chapterService.upDateChapter(request, id);
        return ResponseEntity.ok(ret);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws CustomException {
        chapterService.deleteChapter(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ChapterDto>> getAll() {
        List<ChapterDto> ret = chapterService.getAllChapter();
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<ChapterDto>> paging(@PageableDefault(page = 0, size = 2,sort = "id",direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(required = false) String title) {
        Page<ChapterDto> ret = chapterService.pagingChapterDto(pageable, title);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> get(@PathVariable("id") Long id) throws CustomException {
        ChapterDto ret = chapterService.getChapterDtoById(id);
        return ResponseEntity.ok(ret);
    }
    @GetMapping("/get-chapters-by-course/{courseId}")
    public ResponseEntity<List<ChapterDto>> getChaptersByCourseId(@PathVariable("courseId") Long courseId) {
        List<ChapterDto> ret = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(ret);
    }
}

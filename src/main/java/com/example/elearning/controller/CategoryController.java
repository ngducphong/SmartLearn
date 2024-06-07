package com.example.elearning.controller;


import com.example.elearning.dto.CategoryDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.service.CategoryService;
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

@RequestMapping("/api/v1/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/save")
    public ResponseEntity<CategoryDto> save(@RequestBody CategoryDto request) throws CustomException {
        CategoryDto ret = categoryService.saveCategory(request);
        return ResponseEntity.ok(ret);
    }
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto request, @PathVariable Long id) throws CustomException {
        CategoryDto ret = categoryService.upDateCategory(request, id);
        return ResponseEntity.ok(ret);
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws CustomException {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> ret = categoryService.getAllCategory();
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<CategoryDto>> paging(@PageableDefault(page = 0, size = 2,sort = "id",direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(required = false) String name) {
        Page<CategoryDto> ret = categoryService.pagingCategoryDto(pageable, name);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> get(@PathVariable("id") Long id) throws CustomException {
        CategoryDto ret = categoryService.getCategoryDtoById(id);
        return ResponseEntity.ok(ret);
    }
    @GetMapping("/get-category-by-course/{courseId}")
    public ResponseEntity<CategoryDto> getCategoryByCourseId(@PathVariable("courseId") Long id) throws CustomException {
        CategoryDto ret = categoryService.getCategoryByCourseId(id);
        return ResponseEntity.ok(ret);
    }

}

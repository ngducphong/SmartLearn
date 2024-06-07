package com.example.elearning.service.impl;


import com.example.elearning.dto.CategoryDto;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Chapter;
import com.example.elearning.model.Category;
import com.example.elearning.model.Course;
import com.example.elearning.repository.ChapterRepository;
import com.example.elearning.repository.CategoryRepository;
import com.example.elearning.repository.CourseRepository;
import com.example.elearning.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CourseRepository courseRepository;

    public CategoryDto save(Category entity, CategoryDto dto) throws CustomException {
        entity.setVoided(dto.isVoided());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity = categoryRepository.save(entity);
        return new CategoryDto(entity);
    }

    @Override
    public CategoryDto saveCategory(CategoryDto dto) throws CustomException {
        Category entity = new Category();
        return this.save(entity, dto);
    }

    @Override
    public CategoryDto upDateCategory(CategoryDto dto, Long id) throws CustomException {
        Category Category = categoryRepository.findById(id).orElseThrow(() -> new CustomException("Category not found"));
        return this.save(Category, dto);
    }


    @Override
    public void deleteCategory(Long id) throws CustomException {
        Category Category = categoryRepository.findById(id).orElseThrow(() -> new CustomException("Category not found"));
        if (Category.getVoided() == null || !Category.getVoided()) {
            Category.setVoided(true);
        } else {
            Category.setVoided(false);
        }
        categoryRepository.save(Category);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return categoryRepository.getAll();
    }

    @Override
    public CategoryDto getCategoryDtoById(Long id) throws CustomException {
        return new CategoryDto(this.getCategoryById(id));
    }

    @Override
    public CategoryDto getCategoryByCourseId(Long id) throws CustomException {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CustomException("Course not found"));
        return new CategoryDto(categoryRepository.findById(course.getCategory().getId()).orElseThrow(() -> new CustomException("Category not found")));
    }

    @Override
    public Page<CategoryDto> pagingCategoryDto(Pageable pageable, String name) {
        Page<CategoryDto> page = categoryRepository.getCategoryPage(pageable, name);
        return page;
    }

    private Category getCategoryById(Long id) throws CustomException {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CustomException("Category not found");
    }
}

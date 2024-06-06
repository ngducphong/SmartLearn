package com.example.elearning.service;


import com.example.elearning.dto.CategoryDto;
import com.example.elearning.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    CategoryDto saveCategory(CategoryDto dto) throws  CustomException;

    CategoryDto upDateCategory(CategoryDto dto, Long id) throws CustomException;

    void deleteCategory(Long id) throws CustomException;

    List<CategoryDto> getAllCategory();

    CategoryDto getCategoryDtoById(Long id) throws CustomException;

    Page<CategoryDto> pagingCategoryDto(Pageable pageable, String name);

}

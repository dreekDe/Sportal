package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.category.CategoryCreateDto;
import com.dreekde.sportal.model.dto.category.CategoryDTO;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO addCategory(CategoryCreateDto categoryCreateDto);

    String deleteCategory(long id);
}
package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.category.CategoryCreateDto;
import com.dreekde.sportal.model.dto.category.CategoryDTO;
import com.dreekde.sportal.model.entities.Category;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface CategoryService {

    Category getCategory(long id);

    CategoryDTO getCategoryById(long id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO addCategory(CategoryCreateDto categoryCreateDto);

    long deleteCategory(long id);
}
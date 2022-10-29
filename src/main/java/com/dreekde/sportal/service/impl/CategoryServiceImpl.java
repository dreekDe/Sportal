package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.category.CategoryCreateDto;
import com.dreekde.sportal.model.dto.category.CategoryDTO;
import com.dreekde.sportal.model.entities.Category;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.CategoryRepository;
import com.dreekde.sportal.service.CategoryService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Desislava Tencheva
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_ALREADY_EXIST = "Category already exists!";
    private static final String CATEGORY_NOT_FOUND = "Category not found!";
    private static final String INVALID_CATEGORY = "Invalid category!";

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .filter(Category::isAvailable)
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO addCategory(CategoryCreateDto categoryCreateDto) {
        String categoryName = categoryCreateDto.getCategoryName();
        Optional<Category> currCategory = categoryRepository.findByName(categoryName);
        if (currCategory.isPresent()) {
            if (currCategory.get().isAvailable()) {
                throw new BadRequestException(CATEGORY_ALREADY_EXIST);
            }
            currCategory.get().setAvailable(true);
            categoryRepository.save(currCategory.get());
            return modelMapper.map(currCategory.get(), CategoryDTO.class);
        }
        validateInputString(categoryName);
        Category category = new Category();
        category.setName(categoryName);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public long deleteCategory(long id) {
        Category category = getCategory(id);
        category.setAvailable(false);
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    public Category getCategory(long id) {
        if (id <= 0){
            throw new BadRequestException(INVALID_CATEGORY);
        }
        return categoryRepository.getByIdAndAvailable(true, id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
    }

    private void validateInputString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException(INVALID_CATEGORY);
        }
    }
}




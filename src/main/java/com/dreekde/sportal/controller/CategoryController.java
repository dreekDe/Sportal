package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.category.CategoryCreateDto;
import com.dreekde.sportal.model.dto.category.CategoryDTO;
import com.dreekde.sportal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDTO addCategory(@RequestBody CategoryCreateDto categoryCreateDto,
                                   HttpServletRequest request) {
        validatePermission(request.getSession());
        return categoryService.addCategory(categoryCreateDto);
    }

    @DeleteMapping("/{cid}")
    @ResponseStatus(code = HttpStatus.OK)
    public long deleteCategory(@PathVariable long cid, HttpServletRequest request) {
        validatePermission(request.getSession());
        return categoryService.deleteCategory(cid);
    }
}


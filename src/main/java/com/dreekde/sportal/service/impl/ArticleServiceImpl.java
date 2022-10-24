package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.category.CategoryDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Category;
import com.dreekde.sportal.model.entities.User;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.ArticleRepository;
import com.dreekde.sportal.service.ArticleService;
import com.dreekde.sportal.service.CategoryService;
import com.dreekde.sportal.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * @author Desislava Tencheva
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final String MISSING_TEXT = "Title or text can not be empty!";
    private static final String ARTICLE_DOES_NOT_EXIST = "This article does not exist!";

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(CategoryService categoryService,
                              ModelMapper modelMapper,
                              UserService userService,
                              ArticleRepository articleRepository) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.articleRepository = articleRepository;
    }

    @Override
    public ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO) {
        validateInputString(articleCreateDTO.getTitle());
        validateInputString(articleCreateDTO.getText());
        Article article = modelMapper.map(articleCreateDTO, Article.class);
        article.setViews(0);
        article.setPostDate(LocalDateTime.now());
        article.setCategory(getCategory(articleCreateDTO.getCategory()));
        article.setAuthor(getAuthor(articleCreateDTO.getAuthor()));
        article.setAvailable(true);
        article.setImages(new LinkedList<>());//todo
        articleRepository.save(article);
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Override
    public long deleteArticle(long id) {
        Article article = articleRepository.findById(id).
                orElseThrow(() -> new NotFoundException(ARTICLE_DOES_NOT_EXIST));
        article.setAvailable(false);
        articleRepository.save(article);
        return article.getId();
    }

    private User getAuthor(long id) {
        UserWithoutPasswordDTO userDTO = userService.getUserById(id);
        return modelMapper.map(userDTO, User.class);
    }

    private Category getCategory(long id) {
        CategoryDTO categoryDTO =
                categoryService.getCategoryById(id);
        return modelMapper.map(categoryDTO, Category.class);
    }

    private void validateInputString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException(MISSING_TEXT);
        }
    }
}

package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.ArticleRepository;
import com.dreekde.sportal.service.ArticleService;
import com.dreekde.sportal.service.CategoryService;
import com.dreekde.sportal.service.CommentService;
import com.dreekde.sportal.service.ImageService;
import com.dreekde.sportal.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Desislava Tencheva
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final String MISSING_TEXT = "Title or text can not be empty!";
    private static final String ARTICLE_DOES_NOT_EXIST = "This article does not exist!";
    private static final String INVALID_ARTICLE = "Invalid article!";

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final CommentService commentService;

    @Autowired
    public ArticleServiceImpl(CategoryService categoryService, ModelMapper modelMapper,
                              UserService userService, ArticleRepository articleRepository,
                              ImageService imageService, @Lazy CommentService commentService) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.articleRepository = articleRepository;
        this.imageService = imageService;
        this.commentService = commentService;
    }

    @Override
    public List<ArticleDTO> getTopFiveDailyArticles() {
        List<Article> currArticles = articleRepository
                .topFiveArticles(true, LocalDate.now());
        List<ArticleDTO> topFiveArticles = new LinkedList<>();
        for (Article article : currArticles) {
            topFiveArticles.add(modelMapper.map(article, ArticleDTO.class));
        }
        return topFiveArticles;
    }

    @Transactional
    @Override
    public ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO, MultipartFile file) {
        validateInputString(articleCreateDTO.getTitle());
        validateInputString(articleCreateDTO.getText());
        Article article = modelMapper.map(articleCreateDTO, Article.class);
        article.setViews(0);
        article.setPostDate(LocalDateTime.now());
        article.setCategory(categoryService.getCategory(articleCreateDTO.getCategory()));
        article.setAuthor(userService.getUser(articleCreateDTO.getAuthor()));
        article.setAvailable(true);
        article = articleRepository.save(article);
        imageService.uploadImage(article.getId(), file);
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Transactional
    @Override
    public long deleteArticle(long id) {
        Article article = articleRepository.findById(id).
                orElseThrow(() -> new NotFoundException(ARTICLE_DOES_NOT_EXIST));
        article.setAvailable(false);
        articleRepository.save(article);
        commentService.deleteAllComments(article.getComments());
        return article.getId();
    }

    @Override
    public List<ArticleDTO> getAllArticlesByTitle(String title, int page, int pageSize) {
        validateInputString(title);
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return articleRepository.findAllByTitle(true, title, pageable).stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getAllArticlesByCategory(String category, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<Article> allByCategory = articleRepository
                .findAllByCategory_nameOrderByPostDateDesc(category, pageable);
        return allByCategory.stream()
                .filter(Article::isAvailable)
                .sorted((a, b) -> b.getPostDate().compareTo(a.getPostDate()))
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getAllArticles(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<Article> articles = articleRepository.getAllWithPagination(true, pageable);
        return articles.stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ArticleDetailsDTO getArticleDetailsById(long id) {
        Article article = getArticleById(id);
        article.setViews(article.getViews() + 1);
        articleRepository.save(article);
        ArticleDetailsDTO articleDetailsDTO = modelMapper.map(article, ArticleDetailsDTO.class);
        articleDetailsDTO.setImages(imageService.getAllImagesByArticleId(id));
        articleDetailsDTO.setCountComments(article.getComments().size());
        return articleDetailsDTO;
    }

    @Transactional
    @Override
    public ArticleDTO editArticle(ArticleEditDTO articleEditDTO) {
        validateInputString(articleEditDTO.getTitle());
        validateInputString(articleEditDTO.getText());
        Article article = articleRepository.findById(articleEditDTO.getId())
                .orElseThrow(() -> new NotFoundException(ARTICLE_DOES_NOT_EXIST));
        article.setTitle(articleEditDTO.getTitle());
        article.setText(articleEditDTO.getText());
        article.setAvailable(true);
        article.setViews(0);
        article.setPostDate(LocalDateTime.now());
        article.setCategory(categoryService.getCategory(articleEditDTO.getCategory()));
        article.setAuthor(userService.getUser(articleEditDTO.getAuthor()));
        articleRepository.save(article);
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Override
    public Article getArticleById(long id) {
        if (id <= 0) {
            throw new BadRequestException(INVALID_ARTICLE);
        }
        return articleRepository.getArticleById(true, id).
                orElseThrow(() -> new NotFoundException(ARTICLE_DOES_NOT_EXIST));
    }

    private void validateInputString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException(MISSING_TEXT);
        }
    }
}

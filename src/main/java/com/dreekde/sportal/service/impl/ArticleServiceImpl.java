package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.page.PageRequestByTitle;
import com.dreekde.sportal.model.dto.page.PageRequestDTO;
import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;
import com.dreekde.sportal.model.dto.category.CategoryDTO;
import com.dreekde.sportal.model.dto.page.PageRequestWithCategoryDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Category;
import com.dreekde.sportal.model.entities.User;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.ArticleRepository;
import com.dreekde.sportal.service.ArticleService;
import com.dreekde.sportal.service.CategoryService;
import com.dreekde.sportal.service.ImageService;
import com.dreekde.sportal.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    private static final String FILE_EXIST = "The file already exist!";
    private static final String MISSING_TEXT = "Title or text can not be empty!";
    private static final String ARTICLE_DOES_NOT_EXIST = "This article does not exist!";
    private static final String NOT_UPLOADED = "Upload failed!";

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ArticleRepository articleRepository;
    private final ImageService imageService;

    @Autowired
    public ArticleServiceImpl(CategoryService categoryService, ModelMapper modelMapper,
                              UserService userService, ArticleRepository articleRepository,
                              ImageService imageService) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.articleRepository = articleRepository;
        this.imageService = imageService;
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

    @Override
    public List<ArticleDTO> getAllArticlesByTitle(PageRequestByTitle pageRequestByTitle) {
        Pageable pageable = PageRequest.of(pageRequestByTitle.getPage(),
                pageRequestByTitle.getSizeOfPage());
        String title = pageRequestByTitle.getTitle();
        return articleRepository.findAllByTitle(true, title, pageable).stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getAllArticlesByCategory(PageRequestWithCategoryDTO pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSizeOfPage(),
                Sort.by("postDate").descending());
        List<Article> allByCategory = articleRepository
                .findAllByCategory_id(pageRequest.getCategory(), pageable);
        return allByCategory.stream()
                .filter(Article::isAvailable)
                .sorted((a, b) -> b.getPostDate().compareTo(a.getPostDate()))
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getAllArticles(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSizeOfPage());
        List<Article> articles = articleRepository.getAllWithPagination(true, pageable);
        return articles.stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ArticleDetailsDTO getArticleById(long id) {
        Article article = getArticleId(id);
        article.setViews(article.getViews() + 1);
        articleRepository.save(article);
        return modelMapper.map(article, ArticleDetailsDTO.class);
    }

    @Override
    public ArticleDTO editArticle(ArticleEditDTO articleEditDTO) {
        validateInputString(articleEditDTO.getTitle());
        validateInputString(articleEditDTO.getText());
        Article article = getArticleId(articleEditDTO.getId());
        article.setViews(0);
        article.setPostDate(LocalDateTime.now());
        article.setCategory(getCategory(articleEditDTO.getCategory()));
        article.setAuthor(getAuthor(articleEditDTO.getAuthor()));
        articleRepository.save(article);
        return modelMapper.map(article, ArticleDTO.class);
    }

    @Override
    public long uploadArticleImage(long aid, MultipartFile file) {
        try {
            Article article = getArticleId(aid);
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String name = "uploads" + File.separator + System.nanoTime() + "." + ext;
            File uploadFile = new File(name);
            if (!uploadFile.exists()) {
                Files.copy(file.getInputStream(), uploadFile.toPath());
            } else {
                throw new BadRequestException(FILE_EXIST);
            }
            String canonicalPath = uploadFile.getCanonicalPath();
            return imageService.createImage(article, canonicalPath);
        } catch (IOException e) {
            throw new BadRequestException(NOT_UPLOADED, e);
        }
    }

    private Article getArticleId(long id) {
        return articleRepository.findById(id).
                orElseThrow(() -> new NotFoundException(ARTICLE_DOES_NOT_EXIST));
    }

    private User getAuthor(long id) {
        UserWithoutPasswordDTO userDTO = userService.getUserById(id);
        return modelMapper.map(userDTO, User.class);
    }

    private Category getCategory(long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return modelMapper.map(categoryDTO, Category.class);
    }

    private void validateInputString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException(MISSING_TEXT);
        }
    }
}

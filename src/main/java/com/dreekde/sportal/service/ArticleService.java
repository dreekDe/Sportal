package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;
import com.dreekde.sportal.model.entities.Article;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

    Article getArticleById(long id);

    List<ArticleDTO> getTopFiveDailyArticles();

    ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO, MultipartFile file);

    long deleteArticle(long id);

    List<ArticleDTO> getAllArticlesByCategory(String category, int page, int pageSize);

    List<ArticleDTO> getAllArticles(int page, int pageSize);

    ArticleDetailsDTO getArticleDetailsById(long id);

    ArticleDTO editArticle(ArticleEditDTO articleEditDTO);

    List<ArticleDTO> getAllArticlesByTitle(String title , int page, int pageSize);
}

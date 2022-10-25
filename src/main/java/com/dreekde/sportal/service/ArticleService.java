package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.PageRequestDTO;
import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getTopFiveDailyArticles();

    ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO);

    long deleteArticle(long id);

    List<ArticleDTO> getAllArticlesByCategory(long cid);

    List<ArticleDTO> getAllArticles(PageRequestDTO pageRequestDTO);

    ArticleDetailsDTO getArticleById(long id);

    ArticleDTO editArticle(ArticleEditDTO articleEditDTO);
}

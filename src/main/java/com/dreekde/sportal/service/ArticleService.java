package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;

import java.util.List;

public interface ArticleService {

    ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO);

    long deleteArticle(long id);

    List<ArticleDTO> getAllArticlesByCategory(long cid);

    List<ArticleDTO> getAllArticles();
}

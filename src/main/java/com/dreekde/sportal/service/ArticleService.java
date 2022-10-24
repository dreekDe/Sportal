package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;

public interface ArticleService {

    ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO);
}

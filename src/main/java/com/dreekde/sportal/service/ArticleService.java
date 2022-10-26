package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.page.PageRequestByTitle;
import com.dreekde.sportal.model.dto.page.PageRequestDTO;
import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;
import com.dreekde.sportal.model.dto.page.PageRequestWithCategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getTopFiveDailyArticles();

    ArticleDTO createNewArticle(ArticleCreateDTO articleCreateDTO);

    long deleteArticle(long id);

    List<ArticleDTO> getAllArticlesByCategory(PageRequestWithCategoryDTO pageRequestWithCategoryDTO);

    List<ArticleDTO> getAllArticles(PageRequestDTO pageRequestDTO);

    ArticleDetailsDTO getArticleById(long id);

    ArticleDTO editArticle(ArticleEditDTO articleEditDTO);

    List<ArticleDTO> getAllArticlesByTitle(PageRequestByTitle pageRequestByTitle);

    String uploadArticleImage(long aid, MultipartFile file);
}

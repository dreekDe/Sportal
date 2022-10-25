package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.page.PageRequestByTitle;
import com.dreekde.sportal.model.dto.page.PageRequestDTO;
import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;
import com.dreekde.sportal.model.dto.page.PageRequestWithCategoryDTO;
import com.dreekde.sportal.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController()
@RequestMapping("/articles")
public class ArticleController extends AbstractController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    @GetMapping("/find")
    public List<ArticleDTO> getArticlesByTitle(@RequestBody PageRequestByTitle
                                                           pageRequestByTitle) {
        return articleService.getAllArticlesByTitle(pageRequestByTitle);
    }

    @GetMapping("/topFive")
    public List<ArticleDTO> getTopFiveArticles() {
        return articleService.getTopFiveDailyArticles();
    }

    @GetMapping("/{id}")
    public ArticleDetailsDTO getArticleDetails(@PathVariable long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/all")
    public List<ArticleDTO> getAllArticles(@RequestBody PageRequestDTO pageRequestDTO) {
        return articleService.getAllArticles(pageRequestDTO);
    }

    @GetMapping("/category")
    public List<ArticleDTO> getAllArticleByCategory(@RequestBody PageRequestWithCategoryDTO
                                                    pageRequestWithCategoryDTO) {
        return articleService.getAllArticlesByCategory(pageRequestWithCategoryDTO);
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public ArticleDTO addArticle(@RequestBody ArticleCreateDTO articleCreateDTO,
                                 HttpSession session) {
        validatePermission(session);
        return articleService.createNewArticle(articleCreateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public long deleteArticle(@PathVariable long id, HttpSession session) {
        validatePermission(session);
        return articleService.deleteArticle(id);
    }

    @PutMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public ArticleDTO editArticle(@RequestBody ArticleEditDTO articleEditDTO,
                           HttpSession session) {
        validatePermission(session);
        return articleService.editArticle(articleEditDTO);
    }
}

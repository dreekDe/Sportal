package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/all")
    public List<ArticleDTO> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/categories/{cid}")
    public List<ArticleDTO> getAllArticleByCategory(@PathVariable long cid) {
        return articleService.getAllArticlesByCategory(cid);
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.OK)
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
}

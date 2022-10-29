package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.article.ArticleCreateDTO;
import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.dto.article.ArticleDetailsDTO;
import com.dreekde.sportal.model.dto.article.ArticleEditDTO;
import com.dreekde.sportal.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController()
@RequestMapping("/articles")
public class ArticleController extends AbstractController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/find/{title}/{page}/{pageSize}")
    public List<ArticleDTO> getArticlesByTitle(@PathVariable String title,
                                               @PathVariable int page,
                                               @PathVariable int pageSize) {
        return articleService.getAllArticlesByTitle(title, page, pageSize);
    }

    @GetMapping("/topFive")
    public List<ArticleDTO> getTopFiveArticles() {
        return articleService.getTopFiveDailyArticles();
    }

    @GetMapping("/{id}")
    public ArticleDetailsDTO getArticleDetails(@PathVariable long id) {
        return articleService.getArticleDetailsById(id);
    }

    @GetMapping("/{page}/{pageSize}")
    public List<ArticleDTO> getAllArticles(@PathVariable int page,
                                           @PathVariable int pageSize) {
        return articleService.getAllArticles(page, pageSize);
    }

    @GetMapping("/category/{category}/{page}/{pageSize}")
    public List<ArticleDTO> getAllArticlesByCategory(@PathVariable String category,
                                                     @PathVariable int page,
                                                     @PathVariable int pageSize) {
        return articleService.getAllArticlesByCategory(category, page, pageSize);
    }

    @PostMapping()
    public ArticleDTO addArticle(@ModelAttribute ArticleCreateDTO articleCreateDTO,
                                 @RequestParam(value = "file") MultipartFile file,
                                 HttpSession session) {
        validateAdmin(session);
        return articleService.createNewArticle(articleCreateDTO, file);
    }

    @DeleteMapping("/{id}")
    public long deleteArticle(@PathVariable long id, HttpSession session) {
        validateAdmin(session);
        return articleService.deleteArticle(id);
    }

    @PutMapping()
    public ArticleDTO editArticle(@RequestBody ArticleEditDTO articleEditDTO,
                                  HttpSession session) {
        validateAdmin(session);
        return articleService.editArticle(articleEditDTO);
    }
}

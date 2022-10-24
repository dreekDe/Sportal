package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.dto.article.ArticleDTO;
import com.dreekde.sportal.model.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT * FROM sportal.articles " +
            "WHERE is_available = true ORDER BY post_date DESC",
            nativeQuery = true)
    List<Article> findAll();

    List<Article> findAllByCategory_id(long id);

    @Query(value = "SELECT * FROM sportal.articles" +
            " WHERE date(post_date) = current_date " +
            "AND is_available = true " +
            "ORDER BY views DESC LIMIT 5",
            nativeQuery = true)
    List<Article> topFiveArticles();
}

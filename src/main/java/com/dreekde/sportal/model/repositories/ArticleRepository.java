package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByCategory_id(long id);
}

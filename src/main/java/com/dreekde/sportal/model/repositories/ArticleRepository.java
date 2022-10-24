package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}

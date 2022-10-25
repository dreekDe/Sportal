package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT * FROM sportal.articles " +
            "WHERE is_available = :available " +
            "ORDER BY post_date DESC ",
            countQuery = "SELECT count(*) FROM sportal.articles",
            nativeQuery = true)
    List<Article> getAllWithPagination(@Param("available") boolean available,
                                       Pageable pageable);

    List<Article> findAllByCategory_id(long id, Pageable pageable);

    @Query(value = "SELECT * FROM sportal.articles" +
            " WHERE date(post_date) = :date " +
            "AND is_available = :available " +
            "ORDER BY views DESC LIMIT 5",
            nativeQuery = true)
    List<Article> topFiveArticles(@Param("available") boolean available, @Param("date") LocalDate date);
}

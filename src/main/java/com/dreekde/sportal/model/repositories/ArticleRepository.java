package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    List<Article> findAllByCategory_nameOrderByPostDateDesc(String name, Pageable pageable);

    @Query(value = "SELECT * FROM sportal.articles" +
            " WHERE date(post_date) = :date " +
            "AND is_available = :available " +
            "AND views > 0 " +
            "ORDER BY views DESC , post_date DESC LIMIT 5",
            nativeQuery = true)
    List<Article> topFiveArticles(@Param("available") boolean available,
                                  @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM sportal.articles " +
            "WHERE title LIKE CONCAT('%',:title,'%') " +
            "AND is_available = :available " +
            "ORDER BY post_date DESC ",
            countQuery = "SELECT count(*) FROM sportal.articles",
            nativeQuery = true)
    List<Article> findAllByTitle(@Param("available") boolean available,
                                 @Param("title") String title,
                                 Pageable pageable);

    @Query(value = "SELECT * FROM sportal.articles" +
            " WHERE id = :id " +
            "AND is_available = :available",
            nativeQuery = true)
    Optional<Article> getArticleById(@Param("available") boolean available,
                                     @Param("id") long id);
}

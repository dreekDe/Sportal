package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Desislava Tencheva
 */
@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Query(value = "DELETE FROM sportal.images " +
            "WHERE article_id = :article ",
            nativeQuery = true)
    void deleteAllByArticleId(@Param("article") long id);

}

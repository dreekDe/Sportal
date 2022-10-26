package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByArticle_Id(long id);
}

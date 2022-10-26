package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM sportal.categories" +
            " WHERE id = :id " +
            "AND is_available = :available",
            nativeQuery = true)
    Optional<Category> getByIdAndAvailable(@Param("available") boolean available,
                               @Param("id") long id);

    Optional<Category> findByName(String name);

}

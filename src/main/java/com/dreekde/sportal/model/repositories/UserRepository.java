package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM sportal.users" +
            " WHERE id = :id " +
            "AND is_active = :available",
            nativeQuery = true)
    Optional<User> getUserById(@Param("available") boolean available,
                               @Param("id") long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}

package com.dreekde.sportal.model.repositories;

import com.dreekde.sportal.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByParentIdOrderByPostDateDesc(long id);
}

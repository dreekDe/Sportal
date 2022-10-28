package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.comment.CommentCreateDTO;
import com.dreekde.sportal.model.dto.comment.CommentDTO;
import com.dreekde.sportal.model.entities.Comment;
import com.dreekde.sportal.model.entities.User;
import com.dreekde.sportal.model.exceptions.MethodNotAllowedException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.CommentRepository;
import com.dreekde.sportal.service.ArticleService;
import com.dreekde.sportal.service.CommentService;
import com.dreekde.sportal.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Desislava Tencheva
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final String COMMENT_NOT_FOUND = "Comment not found!";
    private static final String NOT_ALLOWED = "Not allowed operation!";

    private final ModelMapper modelMapper;
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(ModelMapper modelMapper,
                              ArticleService articleService,
                              UserService userService,
                              CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.articleService = articleService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDTO createNewComment(CommentCreateDTO commentCreateDTO) {
        Comment comment = modelMapper.map(commentCreateDTO, Comment.class);
        comment.setPostDate(LocalDateTime.now());
        comment.setArticle(articleService.getArticleById(commentCreateDTO.getArticle()));
        User user = userService.getUser(commentCreateDTO.getOwner());
        comment.setOwner(user);
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public long deleteComment(long id, long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
        if (comment.getOwner().getId() == userId || userService.userIsAdmin(userId)) {
            commentRepository.deleteById(id);
            return comment.getId();
        }
        throw new MethodNotAllowedException(NOT_ALLOWED);
    }
}
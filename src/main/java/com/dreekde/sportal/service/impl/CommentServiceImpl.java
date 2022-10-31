package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.comment.CommentReplyDTO;
import com.dreekde.sportal.model.dto.comment.CommentCreateDTO;
import com.dreekde.sportal.model.dto.comment.CommentDTO;
import com.dreekde.sportal.model.dto.comment.CommentCreateReplyDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Comment;
import com.dreekde.sportal.model.entities.User;
import com.dreekde.sportal.model.exceptions.BadRequestException;
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
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Desislava Tencheva
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final String COMMENT_NOT_FOUND = "Comment not found!";
    private static final String NOT_ALLOWED = "Not allowed operation!";
    private static final String INVALID_COMMENT = "Invalid comment!";
    private static final String METHOD_NOT_ALLOWED = "Can not replay on this comment!";

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
    public List<CommentDTO> getAllCommentsByArticle(long id) {
        List<Comment> comments = articleService.getArticleById(id).getComments();
        return comments.stream()
                .filter(c -> c.getParent() == null)
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setComments(getAllCommentReplies(commentDTO.getId()).size());
                    commentDTO.setLike(comment.getLikers().size());
                    commentDTO.setDislike(comment.getDislikers().size());
                    return commentDTO;
                })
                .collect(Collectors.toList());

    }
    @Override
    public List<CommentReplyDTO> getAllCommentReplies(long id) {
        if (id <= 0) {
            throw new BadRequestException(INVALID_COMMENT);
        }
        List<Comment> allChildComments = commentRepository
                .findAllByParentId(id);
        return allChildComments.stream()
                .map(c ->{
                    CommentReplyDTO commentReplyDTO = modelMapper.map(c, CommentReplyDTO.class);
                    commentReplyDTO.setLike(c.getLikers().size());
                    commentReplyDTO.setDislike(c.getDislikers().size());
                    return commentReplyDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentReplyDTO addReplyComment(CommentCreateReplyDTO commentCreateReplyDTO, long userId) {
        Comment parent = getCommentById(commentCreateReplyDTO.getParent());
        if (parent.getParent() != null) {
            throw new MethodNotAllowedException(METHOD_NOT_ALLOWED);
        }
        Comment comment = createComment(userId, commentCreateReplyDTO.getArticle(),
                commentCreateReplyDTO.getText());
        comment.setParent(parent);
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentReplyDTO.class);
    }

    @Transactional
    @Override
    public CommentDTO createNewComment(CommentCreateDTO commentCreateDTO, long userId) {
        Comment comment = createComment(userId, commentCreateDTO.getArticle(),
                commentCreateDTO.getText());
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public long deleteComment(long id, long userId) {
        Comment comment = getCommentById(id);
        if (comment.getOwner().getId() == userId || userService.userIsAdmin(userId)) {
            commentRepository.deleteById(id);
            return comment.getId();
        }
        throw new MethodNotAllowedException(NOT_ALLOWED);
    }

    @Override
    public int like(long cid, long userId) {
        Comment comment = getCommentById(cid);
        User user = userService.getUser(userId);
        if (comment.getLikers().contains(user)) {
            comment.getLikers().remove(user);
        } else {
            comment.getLikers().add(user);
        }
        comment.getDislikers().remove(user);
        commentRepository.save(comment);
        return comment.getLikers().size();
    }

    @Override
    public int dislike(long cid, long userId) {
        Comment comment = getCommentById(cid);
        User user = userService.getUser(userId);
        if (comment.getDislikers().contains(user)) {
            comment.getDislikers().remove(user);
        } else {
            comment.getDislikers().add(user);
        }
        comment.getLikers().remove(user);
        commentRepository.save(comment);
        return comment.getDislikers().size();
    }

    @Override
    public void deleteAllComments(List<Comment> comments) {
       commentRepository.deleteAll(comments);
    }

    private Comment getCommentById(long id) {
        if (id <= 0) {
            throw new BadRequestException(INVALID_COMMENT);
        }
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
    }

    private Comment createComment(long userId, long aid, String text) {
        Article article = articleService.getArticleById(aid);
        if (text == null || text.isEmpty()) {
            throw new BadRequestException(INVALID_COMMENT);
        }
        Comment comment = new Comment();
        comment.setText(text);
        comment.setPostDate(LocalDateTime.now());
        User user = userService.getUser(userId);
        comment.setOwner(user);
        comment.setArticle(article);
        return comment;
    }
}
package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.comment.CommentReplyDTO;
import com.dreekde.sportal.model.dto.comment.CommentCreateDTO;
import com.dreekde.sportal.model.dto.comment.CommentCreateReplyDTO;
import com.dreekde.sportal.model.dto.comment.CommentDTO;
import com.dreekde.sportal.model.entities.Comment;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface CommentService {

    CommentDTO createNewComment(CommentCreateDTO commentCreateDTO, long userId);

    long deleteComment(long id, long userId);

    List<CommentDTO> getAllCommentsByArticle(long id);

    CommentReplyDTO addReplyComment(CommentCreateReplyDTO commentReplyDTO, long userId);

    List<CommentReplyDTO> getAllCommentReplies(long id);

    int like(long cid, long userId);

    int dislike(long cid, long userId);

    void deleteAllComments(List<Comment> comments);
}

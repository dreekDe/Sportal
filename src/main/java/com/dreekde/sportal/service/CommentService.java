package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.comment.ChildCommentDTO;
import com.dreekde.sportal.model.dto.comment.CommentCreateDTO;
import com.dreekde.sportal.model.dto.comment.CommentDTO;
import com.dreekde.sportal.model.dto.comment.CommentCreateReplayDTO;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface CommentService {

    CommentDTO createNewComment(CommentCreateDTO commentCreateDTO);

    long deleteComment(long id, long userId);

    List<CommentDTO> getAllCommentsByArticle(long id);

    ChildCommentDTO addReplayComment(CommentCreateReplayDTO commentReplayDTO);

}

package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.comment.CommentCreateDTO;
import com.dreekde.sportal.model.dto.comment.CommentDTO;
import com.dreekde.sportal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController
@RequestMapping("/comments")
public class CommentController extends AbstractController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{aid}")
    public List<CommentDTO> getAllCommentsByArticleId(@PathVariable long aid){
        return commentService.getAllCommentsByArticle(aid);
    }

    @PostMapping()
    public CommentDTO createComment(@RequestBody CommentCreateDTO commentCreateDTO,
                                    HttpSession session) {
        getLoggedUserId(session);
        return commentService.createNewComment(commentCreateDTO);
    }

    @DeleteMapping("/{cid}")
    public long deleteComment(@PathVariable long cid, HttpSession session) {
        long userId = getLoggedUserId(session);
        return commentService.deleteComment(cid, userId);
    }
}

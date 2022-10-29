package com.dreekde.sportal.model.dto.comment;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class CommentCreateReplyDTO {

    private long parent;
    private long article;
    private String text;
    private long owner;

}
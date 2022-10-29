package com.dreekde.sportal.model.dto.comment;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class CommentCreateDTO {

    private long article;
    private String text;
}

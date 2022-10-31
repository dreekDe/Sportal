package com.dreekde.sportal.model.dto.comment;

import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class CommentReplyDTO {

    private long id;
    private UserWithoutPasswordDTO owner;
    private String text;
    private int like;
    private int dislike;
}

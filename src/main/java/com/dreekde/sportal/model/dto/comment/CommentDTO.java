package com.dreekde.sportal.model.dto.comment;

import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class CommentDTO {

    private long id;
    private UserWithoutPasswordDTO owner;
    private String text;
   //todo private int likes;
   //todo private int dislikes;
}

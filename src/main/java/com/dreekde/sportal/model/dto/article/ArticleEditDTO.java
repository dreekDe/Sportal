package com.dreekde.sportal.model.dto.article;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class ArticleEditDTO {

    private long id;
    private String title;
    private String text;
    private long category;
    private long author;
}

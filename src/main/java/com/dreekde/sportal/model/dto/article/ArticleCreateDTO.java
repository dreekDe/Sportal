package com.dreekde.sportal.model.dto.article;

import lombok.Data;

@Data
public class ArticleCreateDTO {

    private String title;
    private String text;
    private long category;
    private long author;
}

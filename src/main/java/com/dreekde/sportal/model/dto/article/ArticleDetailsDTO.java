package com.dreekde.sportal.model.dto.article;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@Data
public class ArticleDetailsDTO {

    private long id;
    private String title;
    private String text;
    private int views;
    private LocalDateTime postDate;
    private List<String> images;

}
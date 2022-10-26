package com.dreekde.sportal.model.dto.article;

import com.dreekde.sportal.model.dto.image.ImageDTO;
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
    private List<ImageDTO> images;

}
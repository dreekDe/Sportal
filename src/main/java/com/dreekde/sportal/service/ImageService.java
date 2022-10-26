package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.image.ImageDTO;
import com.dreekde.sportal.model.dto.image.ImageDeleteDTO;
import com.dreekde.sportal.model.entities.Article;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface ImageService {

    String uploadImage(Article article, String canonicalPath);

    long deleteImage(ImageDeleteDTO imageDeleteDTO);

    List<ImageDTO> getAllImagesByArticleId(long id);
}


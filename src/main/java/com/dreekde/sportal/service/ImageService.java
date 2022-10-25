package com.dreekde.sportal.service;

import com.dreekde.sportal.model.entities.Article;

/**
 * @author Desislava Tencheva
 */
public interface ImageService {

    long createImage(Article article, String canonicalPath);
}


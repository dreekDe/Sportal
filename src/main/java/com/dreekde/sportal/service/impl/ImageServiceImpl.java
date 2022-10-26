package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Image;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.ImageRepository;
import com.dreekde.sportal.service.ArticleService;
import com.dreekde.sportal.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * @author Desislava Tencheva
 */
@Repository
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_NOT_FOUND = "Image not found!";

    private final ImageRepository imageRepository;
    private final ArticleService articleService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            @Lazy ArticleService articleService) {
        this.imageRepository = imageRepository;
        this.articleService = articleService;
    }

    @Override
    public long createImage(Article article, String canonicalPath) {
        Image image = new Image();
        image.setImageURL(canonicalPath);
        image.setArticle(article);
        imageRepository.save(image);
        return image.getId();
    }

    @Override
    public long deleteImage(long id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        } else {
            throw new NotFoundException(IMAGE_NOT_FOUND);
        }
        return id;
    }

    @Override
    public long deleteAllImages(long id) {
        int size = articleService.getArticleById(id).getImages().size();
        if (size <= 0) {
            throw new NotFoundException(IMAGE_NOT_FOUND);
        }
        imageRepository.deleteAllByArticleId(id);
        return 0;
    }
}

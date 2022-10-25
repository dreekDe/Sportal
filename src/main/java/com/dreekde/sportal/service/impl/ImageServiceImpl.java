package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.ImageDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Image;
import com.dreekde.sportal.model.repositories.ImageRepository;
import com.dreekde.sportal.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Desislava Tencheva
 */
@Repository
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public long createImage(Article article, String canonicalPath) {
        Image image = new Image();
        image.setImageURL(canonicalPath);
        image.setArticle(article);
        imageRepository.save(image);
        return image.getId();
    }
}

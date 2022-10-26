package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.image.ImageDTO;
import com.dreekde.sportal.model.dto.image.ImageDeleteDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Image;
import com.dreekde.sportal.model.exceptions.MethodNotAllowedException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.ImageRepository;
import com.dreekde.sportal.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Desislava Tencheva
 */
@Repository
public class ImageServiceImpl implements ImageService {

    private static final String NOT_ALLOWED = "Not allowed!";
    private static final String NOT_FOUND = "Image not found!";

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String uploadImage(Article article, String name) {
        Image image = new Image();
        image.setImageURL(name);
        image.setArticle(article);
        imageRepository.save(image);
        return image.getImageURL();
    }

    @Override
    public long deleteImage(ImageDeleteDTO imageDeleteDTO) {
        List<ImageDTO> images = getAllImagesByArticleId(imageDeleteDTO.getArticleId());
        if (images.size() == 1) {
            throw new MethodNotAllowedException(NOT_ALLOWED);
        }
        if (images.size() == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
        long id = imageDeleteDTO.getImageId();
        imageRepository.deleteById(id);
        return id;
    }

    @Override
    public List<ImageDTO> getAllImagesByArticleId(long id) {
        return imageRepository.findAllByArticle_Id(id)
                .stream().map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }
}

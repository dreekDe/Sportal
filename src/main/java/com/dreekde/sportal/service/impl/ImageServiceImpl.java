package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.image.ImageDTO;
import com.dreekde.sportal.model.dto.image.ImageDeleteDTO;
import com.dreekde.sportal.model.entities.Article;
import com.dreekde.sportal.model.entities.Image;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.MethodNotAllowedException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.repositories.ImageRepository;
import com.dreekde.sportal.service.ArticleService;
import com.dreekde.sportal.service.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Desislava Tencheva
 */
@Repository
public class ImageServiceImpl implements ImageService {

    private static final String NOT_UPLOADED = "Upload failed!";
    private static final String FILE_EMPTY = "Not attached file!";
    private static final String FILE_EXIST = "The file already exist!";
    private static final String NOT_ALLOWED = "Not allowed!";
    private static final String NOT_FOUND = "Image not found!";

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final ArticleService articleService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            ModelMapper modelMapper,
                            @Lazy ArticleService articleService) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
        this.articleService = articleService;
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

    @Transactional
    @Override
    public ImageDTO uploadImage(long aid, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException(FILE_EMPTY);
        }
        try {
            Article article = articleService.getArticleById(aid);
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Path.of("uploads");
            String name = System.nanoTime() + "." + ext;
            File uploadFile = new File(String.valueOf(path), name);
            if (!uploadFile.exists()) {
                Files.copy(file.getInputStream(), uploadFile.toPath());
            } else {
                throw new BadRequestException(FILE_EXIST);
            }
            return modelMapper.map(saveImage(article, name),ImageDTO.class);
        } catch (IOException e) {
            throw new BadRequestException(NOT_UPLOADED, e);
        }
    }

    private Image saveImage(Article article, String name) {
        Image image = new Image();
        image.setImageURL(name);
        image.setArticle(article);
        return imageRepository.save(image);
    }
}

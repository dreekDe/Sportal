package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.image.ImageDTO;
import com.dreekde.sportal.model.dto.image.ImageDeleteDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface ImageService {

    ImageDTO uploadImage(long aid, MultipartFile file);

    long deleteImage(ImageDeleteDTO imageDeleteDTO);

    List<ImageDTO> getAllImagesByArticleId(long id);
}


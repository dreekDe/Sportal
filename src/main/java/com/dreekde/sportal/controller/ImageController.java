package com.dreekde.sportal.controller;

import com.dreekde.sportal.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Desislava Tencheva
 */
@RestController()
@RequestMapping("/images")
public class ImageController extends AbstractController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @DeleteMapping("/{id}")
    public long deleteImage(@PathVariable long id, HttpSession session) {
        validatePermission(session);
        return imageService.deleteImage(id);
    }

    @DeleteMapping("/all/{aid}")
    public long deleteAllImagesByArticle(@PathVariable long aid, HttpSession session) {
        validatePermission(session);
        return imageService.deleteAllImages(aid);
    }
}

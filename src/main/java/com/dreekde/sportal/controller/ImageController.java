package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.image.ImageDTO;
import com.dreekde.sportal.model.dto.image.ImageDeleteDTO;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.service.ImageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController()
@RequestMapping("/images")
public class ImageController extends AbstractController {

    private static final String NOT_FOUND = "File not found!";
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/art/{id}")
    public List<ImageDTO> getAllImagesByArticleId(@PathVariable long id){
        return imageService.getAllImagesByArticleId(id);
    }

    @GetMapping("/{imageName}")
    @SneakyThrows
    public void download(@PathVariable String imageName, HttpServletResponse response) {
        File file = new File("uploads" + "/" + imageName);
        System.out.println(file.toPath());
        if (!file.exists()) {
            throw new NotFoundException(NOT_FOUND);
        }
        response.setContentType(Files.probeContentType(file.toPath()));
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @DeleteMapping()
    public long deleteImage(@RequestBody ImageDeleteDTO imageDeleteDTO,
                            HttpSession session) {
        validateAdmin(session);
        return imageService.deleteImage(imageDeleteDTO);
    }
}

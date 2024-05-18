package com.street.core.master_service.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.street.common.utils.ApiResponse;
import com.street.core.master_service.service.AwsS3Service;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Images Management", description = "Images management APIs")
@RestController
@RequestMapping("images")
public class ImageController {

    @Value("${my-config.aws.prefix}")
    private String awsUrl;

    @Autowired
    protected AwsS3Service awsS3Service;

    @SuppressWarnings({"deprecation", "null"})
    @GetMapping(value = "get/**")
    public void viewS3Image(HttpServletResponse response) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String path = request.getServletPath();
        URL url = new URL(awsUrl + path.toString().replaceAll("/images/get", ""));
        BufferedImage image = ImageIO.read(url.openStream());
        response.setHeader("Content-Type", "image/png");
        response.setHeader("Cache-Control", "public, max-age=31536000, must-revalidate");
        ImageIO.write(image, "png", response.getOutputStream());
    }

    @SuppressWarnings("deprecation")
    @GetMapping(value = "get-external/**")
    public void ViewExternalImage(HttpServletResponse response, @RequestParam String url) throws IOException {
        URL urlObj = new URL(url);
        BufferedImage image = ImageIO.read(urlObj.openStream());
        response.setHeader("Content-Type", "image/png");
        ImageIO.write(image, "png", response.getOutputStream());
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping(value = "/upload", name = "UPLOAD IMAGES", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<List<String>> upload(@RequestParam(required = false, defaultValue = "") String folder, @RequestPart("files") MultipartFile[] files) {
        return awsS3Service.uploadAll(folder, files);
    }


    @GetMapping(value = "download")
    public ResponseEntity<?> downloadFile(@RequestParam String filename) throws IOException {
        ByteArrayOutputStream downloadInputStream = awsS3Service.downloadFile(filename);
        return ResponseEntity
                .ok()
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + filename + "\"")
                .body(downloadInputStream.toByteArray());
    }
}

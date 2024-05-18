package com.street.core.master_service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.street.core.auth_service.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AwsS3Service {

    @Value("${my-config.aws.bucketName}")
    private String bucketName;

    @Autowired(required = false)
    protected AmazonS3 amazonS3;

//    public String generatePreSignedUrl(String filePath) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.MINUTE, 10); //validity of 10 minutes
//        return amazonS3.generatePresignedUrl(bucketName, "game-display/" + filePath, calendar.getTime(), HttpMethod.PUT).toString();
//    }

    public String upload(MultipartFile file, String folderName) throws IOException {
        String fileName = UUID.randomUUID() + "_" + System.currentTimeMillis() + "." + getFileExtension(file);
        log.info("Bucket Name is {}", bucketName);
        String keyName = (folderName + "/" + fileName).replaceAll("//", "/");
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        log.info("file is {}", file.getOriginalFilename());
        log.info("keyName is {}", keyName);
        log.info("metadata is {}", metadata);
        log.info("putObjectRequest is {}", putObjectRequest);

        amazonS3.putObject(putObjectRequest);
//        amazonS3.getUrl(bucketName, keyName).toExternalForm();
        return keyName;
    }

    public String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndex + 1);
    }

    public ApiResponse<List<String>> uploadAll(MultipartFile[] files) {
        return doUploadAll("game-display-dev", files);
    }

    public ApiResponse<List<String>> uploadAll(String folder, MultipartFile[] files) {
        return doUploadAll("game-display-dev/" + folder, files);
    }

    private ApiResponse<List<String>> doUploadAll(String folder, MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file -> {
            try {
                fileNames.add(upload(file, folder));
            } catch (IOException e) {
                fileNames.add(e.getCause().getMessage());
                throw new RuntimeException(e);
            }
        });
        return ApiResponse.success(fileNames);
    }

    public ByteArrayOutputStream downloadFile(String fileName) throws IOException {
        final S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        InputStream is = s3Object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[4096];
        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        return outputStream;
    }
}

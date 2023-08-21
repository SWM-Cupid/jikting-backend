package com.cupid.jikting.member.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.FileUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String save(MultipartFile file) throws IOException {
        validateExist(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        String fileName = UUID.randomUUID().toString();
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    private void delete(String url) {
        String key = extractKeyFromImageUrl(url);
        try {
            amazonS3Client.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            throw new FileUploadException(ApplicationError.AWS_S3_DELETE_ERROR);
        }
    }

    public String update(MultipartFile file, String url) throws IOException {
        String savedUrl = save(file);
        delete(url);
        return savedUrl;
    }

    private void validateExist(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException(ApplicationError.FILE_NOT_EXIST);
        }
    }

    private String extractKeyFromImageUrl(String imageUrl) {
        UriComponents components = UriComponentsBuilder.fromUriString(imageUrl).build();
        return components.getPathSegments().get(components.getPathSegments().size() - 1);
    }
}

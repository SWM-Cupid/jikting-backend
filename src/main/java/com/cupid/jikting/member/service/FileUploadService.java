package com.cupid.jikting.member.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.FileUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        String fileName = file.getOriginalFilename();
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    private void validateExist(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException(ApplicationError.FILE_NOT_EXIST);
        }
    }
}

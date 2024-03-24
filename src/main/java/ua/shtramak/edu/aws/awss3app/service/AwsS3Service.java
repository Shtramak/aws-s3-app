package ua.shtramak.edu.aws.awss3app.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AwsS3Service {
    List<String> getAllFileNames();

    byte[] getFileByName(String fileName);

    String getFilePreSignedUrlByName(String fileName);

    void saveFileToBucket(String fileName, MultipartFile file);
}

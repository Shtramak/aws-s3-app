package ua.shtramak.edu.aws.awss3app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.EncodingType;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import ua.shtramak.edu.aws.awss3app.config.AwsBucketNameConfiguration;
import ua.shtramak.edu.aws.awss3app.service.AwsS3Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3ServiceImpl implements AwsS3Service {

    private final S3Client s3Client;

    private final AwsBucketNameConfiguration awsBucketNameConfiguration;

    @Override
    public List<String> getAllFileNames() {
        String readBucketName = getReadBucketName();
        ListObjectsResponse response = s3Client.listObjects(ListObjectsRequest.builder()
                .bucket(readBucketName)
                .delimiter(";\n")
                .encodingType(EncodingType.URL)
                .build());

        return response.contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public byte[] getFileByName(String fileName) {
        String readBucketName = getReadBucketName();
        ResponseInputStream<GetObjectResponse> responseResponseInputStream = s3Client.getObject(GetObjectRequest.builder()
                .bucket(readBucketName)
                .key(fileName)
                .build());

        return responseResponseInputStream.readAllBytes();
    }

    @Override
    public String getFilePreSignedUrlByName(String fileName) {
        String readBucketName = getReadBucketName();
        try (S3Presigner s3Presigner = S3Presigner.create()) {
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5L))
                    .getObjectRequest(GetObjectRequest.builder()
                            .bucket(readBucketName)
                            .key(fileName)
                            .build())
                    .build());

            return presignedGetObjectRequest.url().toString();
        }
    }

    @Override
    @SneakyThrows
    public void saveFileToBucket(String fileName, MultipartFile file) {
        String writeBucketName = awsBucketNameConfiguration.getWrite();
        String key = Optional.ofNullable(fileName).orElseGet(file::getOriginalFilename);
        PutObjectResponse uploadPartResponse = s3Client.putObject(PutObjectRequest.builder()
                .bucket(writeBucketName)
                .key(key)
                .build(), RequestBody.fromBytes(file.getBytes()));
        
        log.info(uploadPartResponse.toString());
    }

    private String getReadBucketName() {
        return awsBucketNameConfiguration.getRead();
    }
}

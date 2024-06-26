package ua.shtramak.edu.aws.awss3app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3ClientConfiguration {
    
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.EU_NORTH_1)
                .build();
    }
}

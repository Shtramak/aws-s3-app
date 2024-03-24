package ua.shtramak.edu.aws.awss3app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.bucket-name")
public class AwsBucketNameConfiguration {
    
    private String write;
    
    private String read;
}

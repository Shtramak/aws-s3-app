package ua.shtramak.edu.aws.awss3app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AwsS3AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsS3AppApplication.class, args);
    }

}

package ua.shtramak.edu.aws.awss3app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.shtramak.edu.aws.awss3app.service.AwsS3Service;

import java.util.List;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @GetMapping
    public ResponseEntity<List<String>> getAllFileNames() {
        return ResponseEntity.ok(awsS3Service.getAllFileNames());
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getFileByName(@PathVariable String fileName) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(awsS3Service.getFileByName(fileName));
    }

    @GetMapping("/{fileName}/pre-signed")
    public ResponseEntity<String> getFilePreSignedUrlByName(@PathVariable String fileName) {
        return ResponseEntity.ok(awsS3Service.getFilePreSignedUrlByName(fileName));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> saveFileToBucket(@RequestParam(value = "fileName", required = false) String fileName,
                                                 @RequestParam(value = "file") MultipartFile file) {

        awsS3Service.saveFileToBucket(fileName, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

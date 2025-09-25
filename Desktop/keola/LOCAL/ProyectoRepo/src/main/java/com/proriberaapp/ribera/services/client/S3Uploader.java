package com.proriberaapp.ribera.services.client;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

public class S3Uploader {
    private final RestTemplate restTemplate;

    public S3Uploader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadToS3(MultipartFile file, int folderNumber) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(convert(file)));
        body.add("folderNumber", folderNumber);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange("https://document-dev.inclub.world/api/v1/s3",
                HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    public Mono<String> uploadToS3AndGetUrl(MultipartFile file, int folderNumber) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(convert(file)));
        body.add("folderNumber", folderNumber);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return Mono.fromCallable(() -> {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://document-dev.inclub.world/api/v1/s3",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return response.getBody();
        });
    }
}
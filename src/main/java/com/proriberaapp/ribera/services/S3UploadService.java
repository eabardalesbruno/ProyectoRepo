package com.proriberaapp.ribera.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class S3UploadService {

    private final WebClient webClient;

    public S3UploadService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://riberams-dev.inclub.world").build();
    }

    public Mono<String> uploadPdf(byte[] pdfData) {
        return Mono.fromCallable(() -> {
            Path tempFile = Files.createTempFile("upload", ".pdf");
            Files.write(tempFile, pdfData);
            return tempFile;
        }).flatMap(tempFile -> {
            return webClient.post()
                    .uri("/api/v1/s3-client/upload")
                    .header("folderNumber", "13")
                    .body(BodyInserters.fromMultipartData("image", tempFile.toFile()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doFinally(signal -> {
                        try {
                            Files.deleteIfExists(tempFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }).onErrorResume(e -> {
            return Mono.error(new RuntimeException("Failed to upload PDF to S3", e));
        });
    }
}
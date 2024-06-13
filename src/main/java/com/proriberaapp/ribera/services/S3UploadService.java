package com.proriberaapp.ribera.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class S3UploadService {

    private final WebClient webClient;

    private final String username;
    private final String password;

    public S3UploadService(WebClient.Builder webClientBuilder,
                           @Value("${s3.client.username}") String username,
                           @Value("${s3.client.password}") String password) {
        this.webClient = webClientBuilder.baseUrl("https://riberams-dev.inclub.world")
                .defaultHeader(HttpHeaders.AUTHORIZATION, createBasicAuthHeader(username, password))
                .build();
        this.username = username;
        this.password = password;
    }

    private String createBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }).onErrorResume(e -> {
            return Mono.error(new RuntimeException("Failed to upload PDF to S3", e));
        });
    }
}
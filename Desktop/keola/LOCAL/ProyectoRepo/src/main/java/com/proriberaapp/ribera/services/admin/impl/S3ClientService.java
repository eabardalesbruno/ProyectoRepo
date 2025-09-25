package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class S3ClientService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final WebClient webClient;

    public S3ClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(uploadDir).build();
    }

    public Mono<S3UploadResponse> upload(Mono<FilePart> file, Integer folderNumber, String token) {
        return file.flatMap(f -> {
            WebClient webClient = WebClient.create();

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", f).contentType(MediaType.MULTIPART_FORM_DATA);
            bodyBuilder.part("folderNumber", folderNumber);

            return webClient.post()
                    .uri(uploadDir)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(S3UploadResponse.class)
                    .map(S3UploadResponse::responseToEntity);
        });
    }

    public Mono<String> uploadFile(Mono<FilePart> file, Integer folderNumber) {
        return file.flatMap(data -> webClient
                .post()
                .uri(uploadDir)
                .body(BodyInserters.fromMultipartData("file", data)
                        .with("folderNumber", folderNumber))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getBoolean("result")) {
                        return Mono.just(jsonResponse.getString("data"));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Error al subir el archivo"));
                    }
                }));
    }

    public Mono<String> deleteFile(String fileUrl) {
        return webClient
                .method(HttpMethod.DELETE)
                .uri(uploadDir)
                .body(BodyInserters.fromFormData("fileUrl", fileUrl))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getBoolean("result")) {
                        return Mono.just("File deleted successfully");
                    } else {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Error al eliminar el archivo"));
                    }
                });
    }
}

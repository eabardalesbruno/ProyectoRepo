package com.proriberaapp.ribera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@Service
public class S3UploadService {

    private final RestTemplate restTemplate;

    @Autowired
    public S3UploadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Mono<String> uploadPdf(File pdfFile, int folderNumber) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(pdfFile));
        body.add("folderNumber", folderNumber); // Asegúrate de agregar folderNumber aquí

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return Mono.fromCallable(() -> {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://documentapi.inclub.world/api/v1/s3",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            //return response.getBody(); ANTES DE PAGO VOUCHER CON PUNTOS
            String responseBody = response.getBody();
            if (responseBody != null) {
                String dataPrefix = "\"data\":\"";
                int startIndex = responseBody.indexOf(dataPrefix) + dataPrefix.length();
                int endIndex = responseBody.indexOf("\"", startIndex);
                if (startIndex > dataPrefix.length() - 1 && endIndex > startIndex) {
                    return responseBody.substring(startIndex, endIndex);
                } else {
                    throw new IOException("Error: sin URL");
                }
            } else {
                throw new IOException("Error: VACIO");
            }
        });
    }
}
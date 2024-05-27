package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.services.admin.impl.S3ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/s3-client")
@RequiredArgsConstructor
public class S3ClientController {
    private final S3ClientService s3ClientService;
    @PostMapping(
            value = "upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<S3UploadResponse> loadBoucher(
            @RequestPart("image") Mono<FilePart> image,
            @RequestHeader("folderNumber") Integer folderNumber,
            @RequestHeader("Authorization") String token
    ) {
        return s3ClientService.upload(image, folderNumber, token);
    }
}

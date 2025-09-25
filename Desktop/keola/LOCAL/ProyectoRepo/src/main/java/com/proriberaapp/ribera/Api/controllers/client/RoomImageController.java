package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RoomImagesEntity;
import com.proriberaapp.ribera.services.admin.impl.S3ClientService;
import com.proriberaapp.ribera.services.client.RoomImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/room-image")
@RequiredArgsConstructor
@Slf4j
public class RoomImageController {

    private final RoomImageService roomImageService;

    private final S3ClientService s3ClientService;

    @GetMapping("/room/{roomId}")
    public Flux<RoomImagesEntity> getRoomImagesByRoomId(@PathVariable Integer roomId) {
        return roomImageService.findAllByRoomId(roomId);
    }

    @GetMapping
    public Flux<RoomImagesEntity> getRoomImages() {
        return roomImageService.findAll();
    }

    @PostMapping("/delete-images")
    public Mono<Void> deleteRoomImage(@RequestBody List<Integer> ids) {
        return roomImageService.deleteAllById(ids);
    }
    @PostMapping(path = "/upload-images/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, List<String>>>> uploadMultipleFiles(
            @RequestPart("files") Flux<FilePart> files,
            @PathVariable Integer id) {
        int maxConcurrency = 5;
        return files
                .flatMap(filePart -> s3ClientService.uploadFile(Mono.just(filePart), 12)
                        .flatMap(filePath -> roomImageService.saveRoomImages(id, filePath)
                                .then(Mono.just(filePath))), maxConcurrency)
                .collectList()
                .map(filePaths -> {
                    Map<String, List<String>> jsonResponse = new HashMap<>();
                    jsonResponse.put("files", filePaths);
                    return ResponseEntity.ok(jsonResponse);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", Collections.singletonList("File upload failed")))));
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Map<String, Object>>> deleteFiles(@RequestBody List<String> fileUrls) {
        return Flux.fromIterable(fileUrls)
                .flatMap(s3ClientService::deleteFile)
                .flatMap(deletedFile ->
                        roomImageService.deleteByImagePath(deletedFile)
                                .then(Mono.just(deletedFile)))
                .collectList()
                .map(deletedFiles -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Files deleted successfully");
                    response.put("deletedFiles", deletedFiles);
                    return ResponseEntity.ok(response);
                });
    }

}

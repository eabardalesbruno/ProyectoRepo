package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.admin.impl.S3ClientService;
import com.proriberaapp.ribera.services.client.RoomService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    private final RoomService roomService;

    private final S3ClientService s3ClientService;

    @GetMapping("/find/{id}")
    public Mono<RoomEntity> findRoom(@PathVariable Integer id) {
        return roomService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<RoomEntity> findAllRooms() {
        return roomService.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<RoomEntity>> createRoom(@RequestBody RoomEntity room) {
        return roomService.createRoom(room)
                .map(createdRoom -> new ResponseEntity<>(createdRoom, HttpStatus.CREATED));
    }

    @PutMapping("/{roomId}")
    public Mono<ResponseEntity<RoomEntity>> updateRoom(@PathVariable Integer roomId, @RequestBody RoomEntity room) {
        return roomService.updateRoom(roomId, room)
                .map(updatedRoom -> new ResponseEntity<>(updatedRoom, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<RoomEntity> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public Mono<ResponseEntity<RoomEntity>> getRoomById(@PathVariable Integer roomId) {
        return roomService.getRoomById(roomId)
                .map(room -> new ResponseEntity<>(room, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{roomId}")
    public Mono<ResponseEntity<Void>> deleteRoom(@PathVariable Integer roomId) {
        return roomService.deleteRoom(roomId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

    @PostMapping(path = "/upload-banner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, String>>> uploadTemporalFile(@RequestPart("file") Mono<FilePart> file, @PathVariable Integer id) {
        return file.flatMap(filePart -> {
            String fileName = filePart.filename();
            return s3ClientService.uploadFile(file, 12)
                    .flatMap(filePath -> roomService.uploadImage(id, filePath)
                            .then(Mono.just(filePath)))
                    .map(filePath -> {
                        Map<String, String> jsonResponse = new HashMap<>();
                        jsonResponse.put("fileName", fileName);
                        jsonResponse.put("filePath", filePath);
                        return ResponseEntity.ok(jsonResponse);
                    });
        }).onErrorResume(e ->
                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "File upload failed")))
        );
    }

}

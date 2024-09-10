package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.client.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    private final RoomService roomService;
    @GetMapping("/find")
    public Mono<RoomEntity> findRoom(@RequestParam Integer id) {
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
}

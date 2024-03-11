package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    private final RoomService roomService;
    @PostMapping("/register")
    public Mono<RoomEntity> registerRoom(@RequestBody RoomEntity roomEntity) {
        return roomService.save(roomEntity);
    }
    @PostMapping("/register/all")
    public Flux<RoomEntity> registerAllRooms(@RequestBody Flux<RoomEntity> roomEntity) {
        return roomService.saveAll(roomEntity);
    }
    @PostMapping("/update")
    public Mono<RoomEntity> updateRoom(@RequestBody RoomEntity roomEntity) {
        return roomService.update(roomEntity);
    }
    @PostMapping("/delete")
    public Mono<Void> deleteRoom(@RequestParam String id) {
        return roomService.deleteById(id);
    }
    @PostMapping("/find")
    public Mono<RoomEntity> findRoom(@RequestParam String id) {
        return roomService.findById(id);
    }
    @PostMapping("/find/all")
    public Flux<RoomEntity> findAllRooms() {
        return roomService.findAll();
    }
}

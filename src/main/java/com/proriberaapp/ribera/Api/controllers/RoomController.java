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
    @GetMapping("/find")
    public Mono<RoomEntity> findRoom(@RequestParam String id) {
        return roomService.findById(id);
    }
    @GetMapping("/find/all")
    public Flux<RoomEntity> findAllRooms() {
        return roomService.findAll();
    }
}

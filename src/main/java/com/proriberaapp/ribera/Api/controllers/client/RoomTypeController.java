package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.client.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/getalltypes")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/all")
    public Flux<RoomTypeEntity> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypes();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RoomTypeEntity>> getRoomTypeById(@PathVariable Integer id) {
        return roomTypeService.findByRoomTypeId(id)
                .map(roomType -> new ResponseEntity<>(roomType, HttpStatus.OK));
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<RoomTypeEntity>> createRoomType(@RequestBody RoomTypeEntity roomTypeEntity) {
        return roomTypeService.createRoomType(roomTypeEntity)
                .map(savedRoomType -> new ResponseEntity<>(savedRoomType, HttpStatus.CREATED))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)));
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<RoomTypeEntity>> updateRoomType(
            @PathVariable Integer id,
            @RequestBody RoomTypeEntity roomTypeEntity) {
        return roomTypeService.updateRoomType(id, roomTypeEntity)
                .map(updatedRoomType -> new ResponseEntity<>(updatedRoomType, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteRoomType(@PathVariable Integer id) {
        return roomTypeService.deleteRoomType(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }
}
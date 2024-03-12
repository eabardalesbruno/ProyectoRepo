package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.Infraestructure.services.RoomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/room-detail")
@RequiredArgsConstructor
public class ManagerRoomDetailController {
    private final RoomDetailService roomDetailService;

    @GetMapping("/find/all")
    public Flux<RoomDetailEntity> findAllRoomDetails() {
        return roomDetailService.findAll();
    }

    @GetMapping("/find")
    public Mono<RoomDetailEntity> findRoomDetail(
            @RequestParam String id
    ) {
        return roomDetailService.findById(id);
    }

    @PostMapping("/register")
    public Mono<RoomDetailEntity> registerRoomDetail(
            @RequestBody RoomDetailEntity roomDetailEntity
    ) {
        return roomDetailService.save(roomDetailEntity);
    }

    @PostMapping("/register/all")
    public Flux<RoomDetailEntity> registerAllRoomDetails(
            @RequestBody Flux<RoomDetailEntity> roomDetailEntity
    ) {
        return roomDetailService.saveAll(roomDetailEntity);
    }

    @PatchMapping("/update")
    public Mono<RoomDetailEntity> updateRoomDetail(
            @RequestBody RoomDetailEntity roomDetailEntity
    ) {
        return roomDetailService.update(roomDetailEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteRoomDetail(
            @RequestParam String id
    ) {
        return roomDetailService.deleteById(id);
    }
}

package com.proriberaapp.ribera.Api.controllers.admin;


import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/room")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomController {
    private final RoomService roomService;
    @PostMapping("/register")
    public Mono<RoomEntity> registerRoom(@RequestBody RoomEntity roomEntity) {
        return roomService.save(roomEntity);
    }
    @PostMapping("/register/all")
    public Flux<RoomEntity> registerAllRooms(@RequestBody List<RoomEntity> roomEntity) {
        log.info("List of rooms: " + roomEntity.toString() );
        return roomService.saveAll(roomEntity);
    }
    @PatchMapping("/update")
    public Mono<RoomEntity> updateRoom(@RequestBody RoomEntity roomEntity) {
        return roomService.update(roomEntity);
    }
    @DeleteMapping("/delete")
    public Mono<Void> deleteRoom(@RequestParam Integer id) {
        return roomService.deleteById(id);
    }
    @GetMapping("/find")
    public Mono<RoomEntity> findRoom(@RequestParam Integer id) {
        return roomService.findById(id);
    }
    @GetMapping("/find/all")
    public Flux<RoomEntity> findAllRooms() {
        return roomService.findAll();
    }
}

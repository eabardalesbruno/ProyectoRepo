package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/room-type")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomTypeController {
    private final RoomTypeService roomTypeService;

    @GetMapping("/find/all")
    public Flux<RoomTypeEntity> getAllRoomTypes() {
        return roomTypeService.findAll();
    }

    @GetMapping("/find")
    public Mono<RoomTypeEntity> getRoomTypeById(Integer id) {
        return roomTypeService.findById(id);
    }

    @PostMapping("/register")
    public Mono<RoomTypeEntity> registerRoomType(
            @RequestBody RoomTypeEntity roomTypeEntity) {
        return roomTypeService.save(roomTypeEntity);
    }

    @PostMapping("/register/all")
    public Flux<RoomTypeEntity> registerAllRoomTypes(
            @RequestBody List<RoomTypeEntity> roomTypeEntity
    ) {
        return roomTypeService.saveAll(roomTypeEntity);
    }

    @PatchMapping("/update")
    public Mono<RoomTypeEntity> updateRoomType() {
        return roomTypeService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteRoomType(Integer id) {
        return roomTypeService.deleteById(id);
    }

}

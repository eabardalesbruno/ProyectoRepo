package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.services.client.RoomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/room-detail")
@RequiredArgsConstructor
public class ManagerRoomDetailController extends BaseManagerController<RoomDetailEntity, RoomDetailEntity> {
    private final RoomDetailService roomDetailService;

    @GetMapping
    public Flux<RoomDetailEntity> findAll() {
        return roomDetailService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<RoomDetailEntity> findById(@PathVariable Integer id) {
        return roomDetailService.findById(id);
    }

    @PostMapping
    public Mono<RoomDetailEntity> create(@RequestBody RoomDetailEntity roomDetailEntity) {
        return roomDetailService.save(roomDetailEntity);
    }

    @PutMapping("/{id}")
    public Mono<RoomDetailEntity> update(@PathVariable Integer id, @RequestBody RoomDetailEntity roomDetailEntity) {
        roomDetailEntity.setRoomDetailId(id);
        return roomDetailService.update(roomDetailEntity);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Integer id) {
        return roomDetailService.deleteById(id);
    }
}

package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import com.proriberaapp.ribera.Infraestructure.services.StateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/state-room")
@RequiredArgsConstructor
@Slf4j
public class StateRoomController {
    private final StateRoomService stateRoomService;

    @GetMapping("/find/all")
    public Flux<StateRoomEntity> findAllStateRooms() {
        return stateRoomService.findAll();
    }

    @GetMapping("/find")
    public Mono<StateRoomEntity> findStateRoom(
            @RequestParam Integer id
    ) {
        return stateRoomService.findById(id);
    }

    @PostMapping("/register")
    public Mono<StateRoomEntity> registerStateRoom(
            @RequestBody StateRoomEntity stateRoomEntity
    ) {
        return stateRoomService.save(stateRoomEntity);
    }

    @PostMapping("/register/all")
    public Flux<StateRoomEntity> registerAllStateRooms(
            @RequestBody List<StateRoomEntity> stateRoomEntity
    ) {
        return stateRoomService.saveAll(stateRoomEntity);
    }

    @PatchMapping("/update")
    public Mono<StateRoomEntity> updateStateRoom(
            @RequestBody StateRoomEntity stateRoomEntity
    ) {
        return stateRoomService.update(stateRoomEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteStateRoom(
            @RequestParam Integer id
    ) {
        return stateRoomService.deleteById(id);
    }
}

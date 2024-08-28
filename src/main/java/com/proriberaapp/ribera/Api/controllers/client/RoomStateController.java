package com.proriberaapp.ribera.Api.controllers.client;
import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import com.proriberaapp.ribera.services.client.RoomStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/roomstates")
@RequiredArgsConstructor
public class RoomStateController {

    private final RoomStateService roomStateService;

    @GetMapping("/all")
    public Flux<RoomStateEntity> getAllRoomStates() {
        return roomStateService.getAllRoomStates();
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<RoomStateEntity>> createRoomState(@RequestBody RoomStateEntity roomStateEntity) {
        return roomStateService.createRoomState(roomStateEntity)
                .map(savedRoomState -> new ResponseEntity<>(savedRoomState, HttpStatus.CREATED));
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<RoomStateEntity>> updateRoomState(
            @PathVariable Integer id,
            @RequestBody RoomStateEntity roomStateEntity) {
        return roomStateService.updateRoomState(id, roomStateEntity)
                .map(updatedRoomState -> new ResponseEntity<>(updatedRoomState, HttpStatus.OK));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteRoomState(@PathVariable Integer id) {
        return roomStateService.deleteRoomState(id)
                .map(r -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
    }
}

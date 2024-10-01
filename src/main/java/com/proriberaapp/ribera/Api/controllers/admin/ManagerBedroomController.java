package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.client.BedroomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bedroom")
@Slf4j
public class ManagerBedroomController extends BaseManagerController<BedroomEntity, BedroomEntity> {
    @Autowired
    private BedroomService bedroomService;

    @GetMapping
    public Flux<BedroomEntity> findAll() {
        return bedroomService.findAll();
    }

    @GetMapping("/roomId/{id}")
    public Flux<BedroomEntity> findByRoomId(@PathVariable Integer id) {
        return bedroomService.findByRoomId(id);
    }

    @PostMapping
    public Flux<BedroomEntity> saveAll(@RequestBody List<BedroomEntity> entity) {
        return bedroomService.saveAll(entity);
    }

    @DeleteMapping("/roomId/{id}")
    public Mono<Void> deleteByRoomId(@PathVariable Integer id) {
        return bedroomService.deleteByRoomId(id);
    }
}

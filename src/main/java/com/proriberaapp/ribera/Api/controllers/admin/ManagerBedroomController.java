package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.BedroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/bedroom")
@RequiredArgsConstructor
public class ManagerBedroomController {
    private final BedroomService bedroomService;

    @GetMapping("find/all")
    public Flux<BedroomEntity> getAllBedrooms() {
        return bedroomService.findAll();
    }

    @GetMapping("find")
    public Mono<BedroomEntity> getBedroomById(@RequestParam Integer id) {
        return bedroomService.findById(id);
    }

    @PostMapping("register")
    public Mono<BedroomEntity> registerBedroom(@RequestBody BedroomEntity bedroomEntity) {
        return bedroomService.save(bedroomEntity);
    }

    @PostMapping("register/all")
    public Flux<BedroomEntity> registerAllBedrooms(@RequestBody List<BedroomEntity> bedroomEntity) {
        return bedroomService.saveAll(bedroomEntity);
    }

    @PatchMapping("update")
    public Mono<BedroomEntity> updateBedroom(@RequestBody BedroomEntity bedroomEntity) {
        return bedroomService.update(bedroomEntity);
    }

    @DeleteMapping("delete")
    public Mono<Void> deleteBedroom(@RequestParam Integer id) {
        return bedroomService.deleteById(id);
    }
}

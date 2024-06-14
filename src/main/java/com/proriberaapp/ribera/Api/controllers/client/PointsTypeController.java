package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.PointsTypeEntity;
import com.proriberaapp.ribera.services.PointsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/points-types")
public class PointsTypeController {
    private final PointsTypeService pointsTypeService;

    @Autowired
    public PointsTypeController(PointsTypeService pointsTypeService) {
        this.pointsTypeService = pointsTypeService;
    }

    @PostMapping
    public Mono<ResponseEntity<PointsTypeEntity>> createPointsType(@RequestBody PointsTypeEntity pointsType) {
        return pointsTypeService.createPointsType(pointsType)
                .map(savedPointsType -> ResponseEntity.status(HttpStatus.CREATED).body(savedPointsType));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PointsTypeEntity>> getPointsTypeById(@PathVariable Integer id) {
        return pointsTypeService.getPointsTypeById(id)
                .map(pointsType -> ResponseEntity.ok(pointsType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PointsTypeEntity> getAllPointsTypes() {
        return pointsTypeService.getAllPointsTypes();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PointsTypeEntity>> updatePointsType(@PathVariable Integer id, @RequestBody PointsTypeEntity pointsType) {
        return pointsTypeService.updatePointsType(id, pointsType)
                .map(updatedPointsType -> ResponseEntity.ok(updatedPointsType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePointsType(@PathVariable Integer id) {
        return pointsTypeService.deletePointsType(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pointstypes/search")
    public Flux<PointsTypeEntity> searchByKeyword(@RequestParam String keyword) {
        return pointsTypeService.findByPointstypedescContaining(keyword);
    }
}

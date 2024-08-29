package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import com.proriberaapp.ribera.services.client.BedsTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bedstype")
public class BedsTypeController {

    private final BedsTypeService bedsTypeService;

    public BedsTypeController(BedsTypeService bedsTypeService) {
        this.bedsTypeService = bedsTypeService;
    }

    @GetMapping("/all")
    public Flux<BedsTypeEntity> getAllBedsTypes() {
        return bedsTypeService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BedsTypeEntity>> getBedsTypeById(@PathVariable Integer id) {
        return bedsTypeService.findById(id)
                .map(bedsType -> new ResponseEntity<>(bedsType, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<BedsTypeEntity>> createBedsType(@RequestBody BedsTypeEntity bedsTypeEntity) {
        return bedsTypeService.save(bedsTypeEntity)
                .map(savedBedsType -> new ResponseEntity<>(savedBedsType, HttpStatus.CREATED))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)));
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<BedsTypeEntity>> updateBedsType(@PathVariable Integer id, @RequestBody BedsTypeEntity bedsTypeEntity) {
        return bedsTypeService.findById(id)
                .flatMap(existingBedsType -> {
                    bedsTypeEntity.setBedTypeId(id);
                    return bedsTypeService.update(bedsTypeEntity)
                            .map(updatedBedsType -> new ResponseEntity<>(updatedBedsType, HttpStatus.OK));
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteBedsType(@PathVariable Integer id) {
        return bedsTypeService.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
}

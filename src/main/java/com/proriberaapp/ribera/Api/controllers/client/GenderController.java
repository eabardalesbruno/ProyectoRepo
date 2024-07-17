package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.GenderEntity;
import com.proriberaapp.ribera.services.client.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/genders")
public class GenderController {

    private final GenderService genderService;

    @Autowired
    public GenderController(GenderService genderService) {
        this.genderService = genderService;
    }

    @PostMapping
    public Mono<GenderEntity> createGender(@RequestBody GenderEntity genderEntity) {
        return genderService.createGender(genderEntity);
    }

    @PutMapping("/{genderId}")
    public Mono<GenderEntity> updateGender(@PathVariable Integer genderId, @RequestBody GenderEntity genderEntity) {
        return genderService.updateGender(genderId, genderEntity);
    }

    @GetMapping("/{genderId}")
    public Mono<GenderEntity> getGenderById(@PathVariable Integer genderId) {
        return genderService.getGenderById(genderId);
    }

    @GetMapping
    public Flux<GenderEntity> getAllGenders() {
        return genderService.getAllGenders();
    }

    @DeleteMapping("/{genderId}")
    public Mono<Void> deleteGender(@PathVariable Integer genderId) {
        return genderService.deleteGender(genderId);
    }
}

package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRateDto;
import com.proriberaapp.ribera.services.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/fullday-rates")
@RequiredArgsConstructor
public class FullDayRateController {

    private final FullDayRateService fullDayRateService;

    @GetMapping("/counter")
    public Mono<Long> getCountAll() {
        return fullDayRateService.countAll();
    }

    @GetMapping("/")
    public Flux<FullDayRateDto> getAllFullDayRate() {
        return fullDayRateService.getRatesAll();
    }

    @GetMapping("/search")
    public Flux<FullDayRateDto> getByFilters(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String type
    ) {
        return fullDayRateService.searchRates(title, status, type);
    }

    @PostMapping("/")
    public Mono<FullDayRateDto> createFullDayRate(@RequestBody FullDayRateDto fullDayRateDto) {
        return fullDayRateService.save(fullDayRateDto);
    }

    @PutMapping("/{id}")
    public Mono<FullDayRateDto> updateFullDayRate(@RequestBody FullDayRateDto fullDayRateDto, @PathVariable Integer id) {
        return fullDayRateService.update(fullDayRateDto, id);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFullDayRate(@PathVariable Integer id) {
        return fullDayRateService.deleteById(id)
                .map(deleted -> deleted
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.notFound().build()
                );
    }

}
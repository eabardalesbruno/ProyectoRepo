package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.dto.FeedingDto;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import com.proriberaapp.ribera.services.client.FeedingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/feeding")
@RequiredArgsConstructor
public class FeedingController {

    private final FeedingService feedingService;

    @GetMapping
    public Flux<FeedingEntity> findAllFeeding() {
        return feedingService.findAllFeeding();
    }

    @GetMapping("/{id}")
    public Mono<FeedingEntity> findFeedingById(@PathVariable Integer id) {
        return feedingService.findFeedingById(id);
    }

    @GetMapping("/detail-roomoffer/{id}")
    public Flux<RoomOfferFeedingEntity> findRoomOfferByFeedingId(@PathVariable Integer id) {
        return feedingService.findRoomOfferByFeedingId(id);
    }
    @PostMapping
    public Mono<FeedingEntity> saveFeeding(@RequestBody FeedingDto feedingDto) {
        return feedingService.saveFeeding(feedingDto);
    }

    @PutMapping
    public Mono<FeedingEntity> updateFeeding(@RequestBody FeedingDto feedingDto) {
        return feedingService.updateFeeding(feedingDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteFeeding(@PathVariable Integer id) {
        return feedingService.deleteFeeding(id);
    }
}

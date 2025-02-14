package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRequest;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.services.client.FullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/fullday")
@RequiredArgsConstructor
public class FullDayController {

    private final FullDayService fullDayService;

    @PostMapping("/register")
    public Mono<ResponseEntity<FullDayEntity>> registerFullDay(@RequestBody FullDayRequest request) {
        return fullDayService.registerFullDay(
                        request.getReceptionistId(),
                        request.getUserPromoterId(),
                        request.getUserClientId(),
                        request.getType(),
                        request.getDetails(),
                        request.getFoods()
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }


}

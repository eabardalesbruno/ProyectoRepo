package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.ExchangeHistoryRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.HistoricalExchangeResponse;
import com.proriberaapp.ribera.Domain.entities.ExchangeHistoryEntity;
import com.proriberaapp.ribera.services.client.ExchangeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.api}/exchange-history")
@RequiredArgsConstructor
public class ExchangeHistoryController {

    private final ExchangeHistoryService exchangeHistoryService;

    @GetMapping("/find-all")
    public Mono<HistoricalExchangeResponse> getAllExchangeHistory(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String exchangeType, @RequestParam(required = false) String serviceType,
            @RequestParam(defaultValue = "10") Integer size,@RequestParam(defaultValue = "0") Integer page) {
        if (userId != null && username != null) {
            return Mono.error(new IllegalArgumentException("No se pueden proporcionar userId y username simultáneamente."));
        } else if (userId != null) {
            // Busca por userId
            return exchangeHistoryService.findByUserId(
                    userId, startDate, endDate, exchangeType, serviceType, size, page);
        } else if (username != null) {
            // Busca por username
            return exchangeHistoryService.findByUsername(
                    username, startDate, endDate, exchangeType, serviceType, size, page);
        } else {
            return Mono.error(new IllegalArgumentException("Se requiere userId o username para la búsqueda."));
        }
    }

    @PostMapping
    public Mono<ExchangeHistoryEntity> createExchangeHistory(@RequestBody ExchangeHistoryRequest request) {
        return exchangeHistoryService.createExchangeHistory(request);
    }
}

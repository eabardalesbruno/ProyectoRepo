package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.NiubizAutorizationEntity;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.client.NiubizService;
import com.proriberaapp.ribera.services.client.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/niubiz")
@RequiredArgsConstructor
@Slf4j
public class NiubizController {
    private final NiubizService niubizService;

    @GetMapping("/getSecurityToken")
    public Mono<String> getSecurityToken() {
        return niubizService.getSecurityToken();
    }

    @PostMapping("/getTokenSession")
    public Mono<Object> getTokenSession(@RequestParam String token, @RequestBody Object req) {
        return niubizService.getTokenSession(token, req);
    }

    @PostMapping("/tofinalize")
    public Mono<ResponseEntity<Void>> tofinalize(@ModelAttribute NiubizAutorizationEntity niubizEntity, @RequestParam String token, @RequestParam Long purchaseNumber, @RequestParam Double amount) {
        return niubizService.tofinalize(niubizEntity, token, purchaseNumber, amount, 1)
                .flatMap(redirect -> {
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, redirect)
                            .build());
                });
    }

    @PostMapping("/endpromote")
    public Mono<ResponseEntity<Void>> endpromote(@ModelAttribute NiubizAutorizationEntity niubizEntity, @RequestParam String token, @RequestParam Long purchaseNumber, @RequestParam Double amount) {
        return niubizService.tofinalize(niubizEntity, token, purchaseNumber, amount, 2)
                .flatMap(redirect -> {
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, redirect)
                            .build());
                });
    }

    @PostMapping("/endclient")
    public Mono<ResponseEntity<Void>> endclient(@ModelAttribute NiubizAutorizationEntity niubizEntity, @RequestParam String token, @RequestParam Long purchaseNumber, @RequestParam Double amount) {
        return niubizService.tofinalize(niubizEntity, token, purchaseNumber, amount, 3)
                .flatMap(redirect -> {
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, redirect)
                            .build());
                });
    }


    @PostMapping("/endFulldayAdmin")
    public Mono<ResponseEntity<Void>> endFulldayAdmin(@ModelAttribute NiubizAutorizationEntity niubizEntity, @RequestParam String token, @RequestParam Long purchaseNumber, @RequestParam Double amount) {
        return niubizService.tofinalize(niubizEntity, token, purchaseNumber, amount, 4)
                .flatMap(redirect -> {
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, redirect)
                            .build());
                });
    }

    @PostMapping("/savePaymentNiubiz")
    Mono<Object> savePayNiubiz(@RequestParam Integer bookingId, @RequestParam String invoiceType, @RequestParam String invoiceDocumentNumber, @RequestParam Double totalDiscount, @RequestParam Double percentageDiscount, @RequestParam Double totalCostWithOutDiscount, @RequestParam Double amount, @RequestParam String transactionId) {
        return niubizService.savePayNiubiz(bookingId, invoiceType, invoiceDocumentNumber, totalDiscount, percentageDiscount, totalCostWithOutDiscount, amount, transactionId);
    }

    @PostMapping("/savePaymentNiubizFullDay")
    public Mono<Object> savePayNiubizFullDay(@RequestParam Integer fullDayId, @RequestParam String invoiceType, @RequestParam String invoiceDocumentNumber, @RequestParam Double totalDiscount, @RequestParam Double percentageDiscount, @RequestParam Double totalCostWithOutDiscount, @RequestParam Double amount, @RequestParam String transactionId) {
        return niubizService.savePayNiubizFullDay(fullDayId, invoiceType, invoiceDocumentNumber, totalDiscount, percentageDiscount, totalCostWithOutDiscount, amount, transactionId);
    }

}

package com.proriberaapp.ribera.Api.controllers.client;


import com.proriberaapp.ribera.Domain.dto.CommissionDTO;
import com.proriberaapp.ribera.Domain.dto.CommissionPromoterDto;
import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.client.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/commission")
@RequiredArgsConstructor
public class CommissionController {

    @Autowired
    private final CommissionService commissionService;


    @PostMapping("/calulatecommision")
    public Mono<ResponseEntity<CommissionEntity>> calculateCommission(@RequestBody PaymentBookEntity paymentBook, @RequestParam Integer caseType) {
        return commissionService.calculateAndSaveCommission(paymentBook,caseType)
                .map(commissionEntity -> new ResponseEntity<>(commissionEntity, HttpStatus.OK));
    }

    @GetMapping("/total-commission")
    public Mono<ResponseEntity<BigDecimal>> getTotalCommissionByPromterId(@RequestParam Integer promoterId) {
        return commissionService.getTotalCommissionByPromoterId(promoterId)
                .map(totalCommission -> ResponseEntity.ok(totalCommission))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/allCommission")
    public Flux<CommissionEntity> getAllCommission() {
        return commissionService.getAllCommission();
    }

    @GetMapping("/commissionPaged")
    public Flux<CommissionDTO> getCommissionsPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return commissionService.getCommissionsPaged(page, size);
    }

    @GetMapping("/commissionAll")
    public Mono<ResponseEntity<List<CommissionEntity>>> getCommissionByPromoterId(@RequestParam Integer promoterId) {
        return commissionService.getCommissionByPromoterId(promoterId)
                .collectList()
                .map(commissions -> ResponseEntity.ok(commissions))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/commission")
    public Mono<CommissionPromoterDto> getCommisionById (@RequestParam Integer commissionId) {
        return commissionService.getCommissionById(commissionId);
    }

    @PutMapping("/{commissionId}/update")
    public Mono<ResponseEntity<CommissionEntity>> updateCommission(@PathVariable Integer commissionId,
                                                                   @RequestParam Integer currencyTypeId,
                                                                   @RequestParam BigDecimal userAmount,
                                                                   @RequestParam String rucNumber,
                                                                   @RequestPart("file") Mono<FilePart> file,
                                                                   @RequestParam Integer folderNumber) {
        return commissionService.updateCommission(commissionId, currencyTypeId, userAmount, rucNumber, file, folderNumber)
                .map(updatedCommission -> ResponseEntity.ok(updatedCommission))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @PutMapping("/{commissionId}/status")
    public Mono<ResponseEntity<CommissionEntity>> updateStatus(
            @PathVariable Integer commissionId,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");
        return commissionService.updateStatusByCommissionId(commissionId, status)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

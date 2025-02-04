package com.proriberaapp.ribera.Api.controllers.client;


import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.client.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/commissionAll")
    public Mono<ResponseEntity<List<CommissionEntity>>> getCommissionByPromoterId(@RequestParam Integer promoterId) {
        return commissionService.getCommissionByPromoterId(promoterId)
                .collectList()
                .map(commissions -> ResponseEntity.ok(commissions))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/commission")
    public Mono<CommissionEntity> getCommisionById (@RequestParam Integer commissionId) {
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

}

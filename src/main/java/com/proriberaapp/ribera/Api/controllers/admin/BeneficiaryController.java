
package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.dto.BeneficiaryDto;
import com.proriberaapp.ribera.services.admin.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    @GetMapping("/{id}/visitas")
    public Mono<Integer> getVisitasBeneficiario(@PathVariable Integer id) {
        return beneficiaryService.getBeneficiaryById(id)
                .map(BeneficiaryDto::getVisitas);
    }

    @PostMapping("/sincronizar-inclub")
    public Mono<Void> sincronizarSociosDesdeInclub() {
        return beneficiaryService.sincronizarSociosDesdeInclub();
    }

    @GetMapping
    public Flux<BeneficiaryDto> getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    @GetMapping("/filter")
    public Flux<BeneficiaryDto> filterBeneficiaries(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String membresia) {
        return beneficiaryService.filterBeneficiaries(nombre, membresia);
    }

    @GetMapping("/{id}")
    public Mono<BeneficiaryDto> getBeneficiaryById(@PathVariable Integer id) {
        return beneficiaryService.getBeneficiaryById(id);
    }

    @PostMapping
    public Mono<BeneficiaryDto> createBeneficiary(@RequestBody BeneficiaryDto dto) {
        return beneficiaryService.createBeneficiary(dto);
    }

    @PutMapping("/{id}")
    public Mono<BeneficiaryDto> updateBeneficiary(@PathVariable Integer id, @RequestBody BeneficiaryDto dto) {
        return beneficiaryService.updateBeneficiary(id, dto);
    }

    @PostMapping("/{id}/visita")
    public Mono<BeneficiaryDto> registrarVisita(@PathVariable Integer id) {
        return beneficiaryService.registrarVisita(id);
    }

    @PostMapping("/{id}/checkin")
    public Mono<BeneficiaryDto> registrarCheckin(@PathVariable Integer id) {
        return beneficiaryService.registrarCheckin(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBeneficiary(@PathVariable Integer id) {
        return beneficiaryService.deleteBeneficiary(id);
    }
}

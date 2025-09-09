
package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Domain.dto.BeneficiaryDto;
import com.proriberaapp.ribera.Domain.entities.BeneficiaryEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeneficiaryService {
    Mono<BeneficiaryDto> createBeneficiary(BeneficiaryDto dto);

    Mono<BeneficiaryDto> updateBeneficiary(Integer id, BeneficiaryDto dto);

    Mono<BeneficiaryDto> registrarVisita(Integer id);

    Mono<BeneficiaryDto> registrarCheckin(Integer id);

    Mono<Void> deleteBeneficiary(Integer id);

    Mono<BeneficiaryDto> getBeneficiaryById(Integer id);

    Flux<BeneficiaryDto> getAllBeneficiaries();

    Flux<BeneficiaryDto> filterBeneficiaries(String nombre, String membresia);

    Flux<com.proriberaapp.ribera.Domain.dto.InclubUserDto> consultarSociosDesdeInclub(String username);

    // BeneficiaryService.java
    Flux<BeneficiaryDto> getBeneficiariesPage(String nombre, int page, int size);
}

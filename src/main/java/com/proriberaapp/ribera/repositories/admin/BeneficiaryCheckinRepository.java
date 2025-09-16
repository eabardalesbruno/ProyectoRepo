package com.proriberaapp.ribera.repositories.admin;

import com.proriberaapp.ribera.entities.admin.BeneficiaryCheckin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeneficiaryCheckinRepository extends ReactiveCrudRepository<BeneficiaryCheckin, Long> {
    Flux<BeneficiaryCheckin> findByIdBeneficiary(String idBeneficiary);
}

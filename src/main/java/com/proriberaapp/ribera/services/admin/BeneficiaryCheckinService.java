package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.entities.admin.BeneficiaryCheckin;
import com.proriberaapp.ribera.dto.admin.BeneficiaryCheckinHistoryDTO;
import com.proriberaapp.ribera.repositories.admin.BeneficiaryCheckinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficiaryCheckinService {
    @Autowired
    private BeneficiaryCheckinRepository repository;

    public Mono<BeneficiaryCheckin> registerCheckin(BeneficiaryCheckin checkin) {
        checkin.setCheckinAt(LocalDateTime.now());
        return repository.save(checkin);
    }

    public Mono<BeneficiaryCheckinHistoryDTO> getCheckinHistory(String idBeneficiary) {
        return repository.findByIdBeneficiary(idBeneficiary)
                .collectList()
                .map(list -> new BeneficiaryCheckinHistoryDTO(
                        idBeneficiary,
                        list.size(),
                        list.stream()
                                .map(bc -> bc.getCheckinAt() != null ? bc.getCheckinAt()
                                        .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                                .filter(ts -> ts != null)
                                .collect(Collectors.toList())));
    }
}

package com.proriberaapp.ribera.controllers.admin;

import com.proriberaapp.ribera.entities.admin.BeneficiaryCheckin;
import com.proriberaapp.ribera.dto.admin.BeneficiaryCheckinHistoryDTO;
import com.proriberaapp.ribera.services.admin.BeneficiaryCheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/checkin")
public class BeneficiaryCheckinController {
    @Autowired
    private BeneficiaryCheckinService service;

    // Registrar un check-in
    @PostMapping("/{idBeneficiary}")
    public Mono<ResponseEntity<BeneficiaryCheckin>> checkinBeneficiary(@PathVariable String idBeneficiary,
            @RequestBody BeneficiaryCheckin body) {
        body.setIdBeneficiary(idBeneficiary);
        return service.registerCheckin(body)
                .map(saved -> ResponseEntity.ok(saved))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    // Consultar historial y visitas
    @GetMapping("/{idBeneficiary}")
    public Mono<BeneficiaryCheckinHistoryDTO> getCheckinHistory(@PathVariable String idBeneficiary) {
        return service.getCheckinHistory(idBeneficiary);
    }






}

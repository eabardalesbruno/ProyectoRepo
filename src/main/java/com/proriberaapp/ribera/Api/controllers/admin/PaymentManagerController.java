package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentResponse;
import com.proriberaapp.ribera.Infraestructure.services.admin.PaymentManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/user/admin/manager/payment")
@RequiredArgsConstructor
public class PaymentManagerController {
    private final PaymentManagerService paymentManagerService;

    @GetMapping("/find/all")
    public Flux<PaymentResponse> findAll() {
        return paymentManagerService.listPayments();
    }
}

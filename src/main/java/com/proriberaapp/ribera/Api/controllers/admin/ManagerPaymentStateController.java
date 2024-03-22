package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.PaymentStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/payment-state")
@RequiredArgsConstructor
public class ManagerPaymentStateController extends BaseManagerController<PaymentStateEntity, PaymentStateEntity>{
    private final PaymentStateService paymentStateService;

}

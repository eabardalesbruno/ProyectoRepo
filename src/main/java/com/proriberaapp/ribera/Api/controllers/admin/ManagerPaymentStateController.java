package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.services.PaymentStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/payment-state")
@RequiredArgsConstructor
public class ManagerPaymentStateController extends BaseManagerController<PaymentStateEntity, PaymentStateEntity>{
    private final PaymentStateService paymentStateService;

}

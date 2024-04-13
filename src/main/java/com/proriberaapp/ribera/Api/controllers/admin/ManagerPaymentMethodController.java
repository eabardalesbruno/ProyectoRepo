package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/payment-method")
@RequiredArgsConstructor
public class ManagerPaymentMethodController extends BaseManagerController<PaymentMethodEntity, PaymentMethodRequest>{
    private final PaymentMethodService paymentMethodService;

}

package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodService extends BaseService<PaymentMethodEntity,PaymentMethodRequest> {
}

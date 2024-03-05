package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Infraestructure.repository.PaymentManagerRespository;
import com.proriberaapp.ribera.Infraestructure.services.admin.PaymentManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentManagerServiceImpl implements PaymentManagerService {
    private final PaymentManagerRespository paymentManagerRespository;

}

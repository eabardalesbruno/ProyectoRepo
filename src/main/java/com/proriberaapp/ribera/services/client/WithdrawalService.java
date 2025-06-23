package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.WithdrawRequestDTO;
import com.proriberaapp.ribera.Domain.entities.WithdrawalRequestEntity;
import reactor.core.publisher.Mono;

public interface WithdrawalService {

    /**
     * Crea una nueva solicitud de retiro para un usuario.
     *
     * @param requestDTO Los datos de la solicitud de retiro que vienen del cliente.
     * @param userId El ID del usuario que está realizando la solicitud.
     * @return Un Mono que emite la entidad de la solicitud de retiro creada y guardada.
     */
    Mono<WithdrawalRequestEntity> createWithdrawalRequest(WithdrawRequestDTO requestDTO, Integer userId);


} 
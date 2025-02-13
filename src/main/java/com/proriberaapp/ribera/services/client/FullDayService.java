package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.FullDayDetailEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FullDayService {

    Mono<FullDayEntity> registerFullDay(Integer receptionistId, Integer userPromoterId, Integer userClientId, String type, List<FullDayDetailEntity> details);
}

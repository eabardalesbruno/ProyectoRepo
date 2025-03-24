package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.FullDayDetailEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayFoodEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FullDayService {

    Mono<FullDayEntity> registerFullDay(Integer receptionistId, Integer userPromoterId, Integer userClientId, String type, List<FullDayDetailEntity> details, List<FullDayFoodEntity> foods);

    Mono<Void> saveFood(List<FullDayDetailEntity> savedDetails, List<FullDayFoodEntity> foods);

}

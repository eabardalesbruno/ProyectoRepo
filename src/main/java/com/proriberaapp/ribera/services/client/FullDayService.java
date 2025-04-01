package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.dto.FoodDetailVisualCountDto;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailFulldayDTO;
import com.proriberaapp.ribera.Domain.dto.VisualCountDetailsDTO;
import com.proriberaapp.ribera.Domain.entities.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

public interface FullDayService {

    Mono<FullDayEntity> registerFullDay(Integer receptionistId, Integer userPromoterId, Integer userClientId, String type, Timestamp bookingdate, List<FullDayDetailEntity> details, List<FullDayFoodEntity> foods, List<MembershipDetail> membershipDetails);

    Mono<Void> saveFood(List<FullDayDetailEntity> savedDetails, List<FullDayFoodEntity> foods);

    Flux<FullDayEntity> getReservationsByAssociatedId(Integer id, String filterType, Integer bookingStateId);

    Mono<FullDayEntity> findById(Integer id);

    Flux<FullDayEntity> getReservationsAll();

    Flux<PaymentDetailFulldayDTO> getPaymentDetailFullday();

    Flux<FoodDetailVisualCountDto> getPaymentDetails(Integer bookingId);

    Mono<VisualCountDetailsDTO> getVisualCountDetails(Integer bookingId);

    Mono<UserClientEntity> getUserclientFullday(Integer userId);

}

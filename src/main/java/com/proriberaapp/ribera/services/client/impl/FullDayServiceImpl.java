package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.FoodDetailVisualCountDto;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailFulldayDTO;
import com.proriberaapp.ribera.Domain.dto.VisualCountDetailsDTO;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.CommissionService;
import com.proriberaapp.ribera.services.client.FullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FullDayServiceImpl implements FullDayService {

    private final FullDayRepository fullDayRepository;

    private final FullDayDetailRepository fullDayDetailRepository;

    private final FullDayFoodRepository fullDayFoodRepository;

    private final CommissionRepository commissionRepository;

    private final CommissionService commissionService;

    private final TicketEntryFullDayRepository ticketEntryFullDayRepository;

    private final FullDayTypeFoodRepository fullDayTypeFoodRepository;

    private final UserClientRepository userRepository;

    private final PaymentBookRepository paymentBookRepository;

    @Override
    public Mono<FullDayEntity> registerFullDay(Integer receptionistId, Integer userPromoterId, Integer userClientId, String type, Timestamp bookingdate,
                                               List<FullDayDetailEntity> details, List<FullDayFoodEntity> foods) {
        FullDayEntity fullDay = FullDayEntity.builder()
                .receptionistId(receptionistId)
                .userPromoterId(userPromoterId)
                .userClientId(userClientId)
                .type(type)
                .purchaseDate(Timestamp.from(Instant.now()))
                .totalPrice(BigDecimal.ZERO)
                .bookingstateid(3)
                .bookingDate(bookingdate)
                .build();

        return fullDayRepository.save(fullDay)
                .flatMap(savedEntity -> Flux.fromIterable(details)
                        .flatMap(detail -> {
                            detail.setFulldayid(savedEntity.getFulldayid());
                            return calcularPrecios(detail, type, foods)
                                    .flatMap(fullDayDetailRepository::save);
                        })
                        .flatMap(fullDayDetailRepository::save)
                        .collectList()
                        .flatMap(savedDetails -> {
                            if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                                return saveFood(savedDetails, foods).thenReturn(savedDetails);
                            }
                            return Mono.just(savedDetails);
                        })
                        .flatMap(savedDetails -> {
                            BigDecimal totalPrice = savedDetails.stream()
                                    .map(FullDayDetailEntity::getFinalPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            savedEntity.setTotalPrice(totalPrice);

                            return fullDayRepository.save(savedEntity);
                        })
                        .flatMap(updatedEntity -> {
                            if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                                return fullDayRepository.findById(updatedEntity.getFulldayid())
                                        .flatMap(existingFullDay -> calculateAndSaveCommission(existingFullDay, details));
                            }
                            return Mono.just(updatedEntity);
                        })
                );
    }

    @Override
    public Mono<Void> saveFood(List<FullDayDetailEntity> savedDetails, List<FullDayFoodEntity> foods) {
        if (savedDetails.isEmpty() || foods.isEmpty()) {
            return Mono.empty();
        }
        return Flux.fromIterable(savedDetails)
                .flatMap(detail -> Flux.fromIterable(detail.getFulldayTypefoodid())
                        .flatMap(foodId -> {
                            Optional<FullDayFoodEntity> matchingFood = foods.stream()
                                    .filter(food -> food.getFulldayTypefoodid().equals(foodId))
                                    .findFirst();

                            if (matchingFood.isPresent()) {
                                FullDayFoodEntity food = matchingFood.get();

                                FullDayFoodEntity newFoodEntry = new FullDayFoodEntity();
                                newFoodEntry.setFulldaydetailid(detail.getFulldaydetailid());
                                newFoodEntry.setFulldayTypefoodid(foodId);
                                newFoodEntry.setQuantity(food.getQuantity());

                                return fullDayFoodRepository.save(newFoodEntry);
                            } else {
                                return Mono.error(new IllegalStateException("No se encontró una comida válida para asignar."));
                            }
                        })
                ).then();
    }

    @Override
    public Flux<FullDayEntity> getReservationsByAssociatedId(Integer id, String filterType, Integer bookingStateId) {
        switch (filterType) {
            case "receptionist":
                return fullDayRepository.findByReceptionistIdAndBookingStateId(id, bookingStateId);
            case "promoter":
                return fullDayRepository.findByUserPromoterIdAndBookingStateId(id, bookingStateId);
            case "client":
                return fullDayRepository.findByUserClientIdAndBookingStateId(id, bookingStateId);
            default:
                return Flux.empty();
        }
    }

    private Mono<FullDayDetailEntity> calcularPrecios(FullDayDetailEntity detail, String type, List<FullDayFoodEntity> foods) {
        return getBasePrice(detail.getTypePerson())
                .flatMap(basePrice -> {
                    BigDecimal discountPerUnit = type.equalsIgnoreCase("Full Day Todo Completo")
                            ? basePrice.multiply(BigDecimal.valueOf(0.50))
                            : BigDecimal.ZERO;

                    BigDecimal totalDiscount = discountPerUnit.multiply(BigDecimal.valueOf(detail.getQuantity()));

                    if (detail.getFulldayTypefoodid() == null || detail.getFulldayTypefoodid().isEmpty()) {
                        BigDecimal foodPrice = getDefaultFoodPrice(detail.getTypePerson()).multiply(BigDecimal.valueOf(detail.getQuantity()));
                        BigDecimal finalPrice = basePrice.multiply(BigDecimal.valueOf(detail.getQuantity())).subtract(totalDiscount).add(foodPrice);

                        detail.setBasePrice(basePrice);
                        detail.setFoodPrice(foodPrice);
                        detail.setDiscountApplied(totalDiscount);
                        detail.setFinalPrice(finalPrice);

                        return Mono.just(detail);
                    }

                    return Flux.fromIterable(detail.getFulldayTypefoodid())
                            .flatMap(foodId -> getFoodPriceById(foodId)
                                    .map(price -> new AbstractMap.SimpleEntry<>(foodId, price)))
                            .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                            .flatMap(foodPriceMap -> {
                                BigDecimal totalFoodPrice = BigDecimal.ZERO;

                                for (FullDayFoodEntity food : foods) {
                                    if (foodPriceMap.containsKey(food.getFulldayTypefoodid())) {
                                        BigDecimal pricePerUnit = foodPriceMap.get(food.getFulldayTypefoodid());
                                        totalFoodPrice = totalFoodPrice.add(pricePerUnit.multiply(BigDecimal.valueOf(food.getQuantity())));
                                    }
                                }
                                BigDecimal finalPrice = totalDiscount.add(totalFoodPrice);

                                detail.setBasePrice(basePrice);
                                detail.setFoodPrice(totalFoodPrice);
                                detail.setDiscountApplied(totalDiscount);
                                detail.setFinalPrice(finalPrice);

                                return Mono.just(detail);
                            });
                });
    }

    private BigDecimal getDefaultFoodPrice(String typePerson) {
        switch (typePerson.toUpperCase()) {
            case "ADULTO":
                return BigDecimal.valueOf(50);
            case "ADULTO_MAYOR":
            case "NINO":
                return BigDecimal.valueOf(35);
            case "INFANTE":
                return BigDecimal.ZERO;
            default:
                return BigDecimal.ZERO;
        }
    }

    private Mono<BigDecimal> getFoodPriceById(Integer fulldayTypefoodid) {
        return fullDayTypeFoodRepository.findById(fulldayTypefoodid)
                .map(FullDayTypeFoodEntity::getPrice)
                .defaultIfEmpty(BigDecimal.ZERO);
    }

    private Mono<BigDecimal> getBasePrice(String typePerson) {
        return ticketEntryFullDayRepository.findByTicketEntryFullDayId(1)
                .flatMap(ticket -> {
                    switch (typePerson.toUpperCase()) {
                        case "ADULTO":
                            return Mono.just(ticket.getAdultPrice());
                        case "ADULTO_MAYOR":
                            return Mono.just(ticket.getSeniorPrice());
                        case "NINO":
                            return Mono.just(ticket.getChildPrice());
                        case "INFANTE":
                            return Mono.just(ticket.getInfantPrice());
                        default:
                            return Mono.error(new IllegalArgumentException("Tipo de persona no válido"));
                    }
                });
    }

   /* private Mono<Boolean> verificarSocioYValidarInvitados(Integer userClientId, List<FullDayDetailEntity> details) {
        return userRepository.findById(userClientId)
                .flatMap(user -> {
                    if (!user.isUserInclub()) {
                        return Mono.just(true);
                    }

                    return obtenerLimiteInvitadosPorMembresia(user.getMembresia())
                            .map(limite -> {
                                long cantidadInvitados = details.stream()
                                        .filter(detail -> !detail.getIsTitular())
                                        .mapToLong(FullDayDetailEntity::getQuantity)
                                        .sum();
                                return cantidadInvitados <= limite;
                            });
                })
                .defaultIfEmpty(true);
    }

    private Mono<Integer> obtenerLimiteInvitadosPorMembresia(String membresia) {
        int limite;
        switch (membresia.toUpperCase()) {
            case "MINI":
            case "EXPERIENCE":
            case "LIGHT":
            case "STANDARD":
            case "VITALICIA":
                limite = 4;
                break;
            case "VITALICIA PREMIUM":
                limite = 8;
                break;
            case "VITALICIA ULTRA PREMIUM":
                limite = 16;
                break;
            default:
                limite = 0;
        }
        return Mono.just(limite);
    }

    */


    private Mono<FullDayEntity> calculateAndSaveCommission(FullDayEntity fullDay, List<FullDayDetailEntity> details) {
        BigDecimal commissionAmount = BigDecimal.ZERO;

        for (FullDayDetailEntity detail : details) {
            BigDecimal commissionRate = getCommissionRate(detail.getTypePerson());
            BigDecimal calculatedCommission = commissionRate.multiply(BigDecimal.valueOf(detail.getQuantity()));
            commissionAmount = commissionAmount.add(calculatedCommission);
        }

        if (commissionAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.just(fullDay);
        }

        Timestamp createdAt = Timestamp.from(Instant.now());
        Timestamp disbursementDate = calculateDisbursementDate(createdAt);

        CommissionEntity commission = new CommissionEntity();
        commission.setPromoterId(fullDay.getUserPromoterId());
        commission.setReceptionistId(fullDay.getReceptionistId());
        commission.setCommissionAmount(commissionAmount);
        commission.setRiberaAmount(fullDay.getTotalPrice());
        commission.setCaseType(1);
        commission.setPartnerPayment(BigDecimal.ZERO);
        commission.setAdminFee(BigDecimal.ZERO);
        commission.setServiceFee(BigDecimal.ZERO);
        commission.setStatus("Pendiente");
        commission.setFullDayId(fullDay.getFulldayid());

        commission.setDayBookingInit(fullDay.getPurchaseDate());
        commission.setCreatedAt(createdAt);
        commission.setDisbursementDate(disbursementDate);
        return commissionService.generateSerialNumber()
                .doOnNext(commission::setSerialNumber)
                .then(commissionRepository.save(commission))
                .thenReturn(fullDay);
    }

    private BigDecimal getCommissionRate(String typePerson) {
        if (typePerson == null || typePerson.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        String normalizedType = typePerson.trim().toUpperCase();
        BigDecimal rate;
        switch (normalizedType) {
            case "ADULTO":
                rate = BigDecimal.valueOf(10);
                break;
            case "NINO":
            case "ADULTO_MAYOR":
                rate = BigDecimal.valueOf(7.50);
                break;
            default:
                rate = BigDecimal.ZERO;
        }
        return rate;
    }

    private Timestamp calculateDisbursementDate(Timestamp createdAt) {
        LocalDate createdDate = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate disbursementDate;
        if (createdDate.getDayOfMonth() <= 15) {
            disbursementDate = LocalDate.of(createdDate.getYear(), createdDate.getMonth(), 20);
        } else {
            disbursementDate = LocalDate.of(createdDate.plusMonths(1).getYear(), createdDate.plusMonths(1).getMonth(), 5);
        }
        return Timestamp.valueOf(disbursementDate.atStartOfDay());
    }

@Override
    public Flux<FoodDetailVisualCountDto> getPaymentDetails(Integer bookingId) {
        return paymentBookRepository.findPaymentDetailsByBookingId(bookingId);
    }

    @Override
    public Mono<VisualCountDetailsDTO> getVisualCountDetails(Integer bookingId){
        return paymentBookRepository.findBookingDetailsByBookingId(bookingId)
                .map(dto -> {
                    String rangoFechasEnEspanol = dto.getRangoFechas()
                            .replace("January", "ENERO")
                            .replace("February", "FEBRERO")
                            .replace("March", "MARZO")
                            .replace("April", "ABRIL ")
                            .replace("May", "MAYO")
                            .replace("June", "JUNIO")
                            .replace("July", "JULIO")
                            .replace("August", "AGOSTO")
                            .replace("September", "SEPTIEMBRE")
                            .replace("October", "OCTUBRE")
                            .replace("November", "NOVIEMBRE")
                            .replace("December", "DICIEMBRE");

                    dto.setRangoFechas(rangoFechasEnEspanol);
                    return dto;
                });
    }

    @Override
    public Mono<UserClientEntity> getUserclientFullday(Integer userId) {
        return fullDayRepository.findByUserclientid(userId)
                .map(user -> {
                    if (user.getRole() == null) {
                        user.setRole(1);
                    }
                    return user;
                });
    }

    @Override
    public Mono<FullDayEntity> findById(Integer id) {
        return fullDayRepository.findById(id);
    }

    @Override
    public Flux<FullDayEntity> getReservationsAll() {
        return fullDayRepository.findAll();
    }

    @Override
    public Flux<PaymentDetailFulldayDTO> getPaymentDetailFullday() {
        return fullDayRepository.findByAllPayment();
    }


}
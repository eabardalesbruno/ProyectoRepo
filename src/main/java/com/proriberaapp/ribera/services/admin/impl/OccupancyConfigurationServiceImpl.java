package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.request.OccupancyByOccupancyAndDaysRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.request.OccupancyDayRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyByOccupancyAndDaysDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyByOccupancyAndDaysResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyDayDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.request.OccupancyByRangesRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.response.OcupancyByRangesResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.request.StandByRulesRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.*;
import com.proriberaapp.ribera.Domain.entities.OccupancyByRangeEntity;
import com.proriberaapp.ribera.Domain.entities.OccupancyDayEntity;
import com.proriberaapp.ribera.Domain.entities.OccupancyEntity;
import com.proriberaapp.ribera.Domain.entities.StandbyRuleEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.admin.OccupancyConfigurationService;
import com.proriberaapp.ribera.utils.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OccupancyConfigurationServiceImpl implements OccupancyConfigurationService {

    private final OccupancyByRangeRepository occupancyByRangeRepository;
    private final OccupancyRepository occupancyRepository;
    private final OccupancyDayRepository occupancyDayRepository;
    private final StandbyRuleRepository standbyRuleRepository;
    private final ReservationTimeTypeRepository reservationTimeTypeRepository;
    private final VisibilityTypeRepository visibilityTypeRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<OccupancyByOccupancyAndDaysResponse> getListByOccupancyAndDaysWithPagination(String searchTerm, Integer size, Integer page) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        Integer offset = page * size;
        Mono<Integer> totalMono = occupancyRepository.countListOccupancyByOccupancyAndDays(searchTerm);
        Mono<List<OccupancyEntity>> dataMono = occupancyRepository.getListByOccupancyAndDays(searchTerm, size, offset)
                .collectList();

        Integer finalSize = size;
        Integer finalPage = page;
        return Mono.zip(totalMono, dataMono)
                .map(tuple -> {
                    Integer total = tuple.getT1();
                    List<OccupancyEntity> data = tuple.getT2();
                    boolean result = !data.isEmpty();
                    return OccupancyByOccupancyAndDaysResponse.builder()
                            .result(result)
                            .total(total)
                            .data(data)
                            .size(finalSize)
                            .page(finalPage)
                            .build();
                })
                .switchIfEmpty(Mono.just(OccupancyByOccupancyAndDaysResponse.builder()
                        .result(false)
                        .total(0)
                        .data(List.of())
                        .size(size)
                        .page(page)
                        .build()));
    }

    @Override
    public Mono<OccupancyByOccupancyAndDaysDetailDto> getOccupancByOccupancyAndDaysById(Integer occupancyId) {
        return occupancyRepository.findById(occupancyId)
                .switchIfEmpty(Mono.empty())
                .flatMap(occupancyEntity -> occupancyDayRepository.findByOccupancyId(occupancyId)
                        .map(occupancyDay -> OccupancyDayDto.builder()
                                .dayId(occupancyDay.getDayId())
                                .status(occupancyDay.getStatus())
                                .build())
                        .collectList()
                        .map(occupancyDayDtoList -> OccupancyByOccupancyAndDaysDetailDto.builder()
                                .id(occupancyEntity.getId())
                                .ruleName(occupancyEntity.getRuleName())
                                .description(occupancyEntity.getDescription())
                                .cashPercentage(occupancyEntity.getCashPercentage())
                                .rewardsPercentage(occupancyEntity.getRewardsPercentage())
                                .status(occupancyEntity.getStatus())
                                .occupancyDayList(occupancyDayDtoList)
                                .build()));
    }

    @Override
    public Mono<OccupancyByOccupancyAndDaysDetailDto> createOccupancyByOccupancyAndDaysEntity(OccupancyByOccupancyAndDaysRequest request) {
        var occupancyByOccupancyAndDaysEntity = OccupancyEntity.builder()
                .ruleName(request.getRuleName())
                .description(request.getDescription())
                .cashPercentage(request.getCashPercentage())
                .rewardsPercentage(request.getRewardsPercentage())
                .status(Constants.ACTIVE)
                .createdAt(LocalDateTime.now())
                .createdBy(Constants.USER_ADMIN)
                .build();
        return occupancyRepository.save(occupancyByOccupancyAndDaysEntity)
                .flatMap(savedOccupancyEntity -> {
                    List<OccupancyDayRequest> dayRequests = request.getOccupancyDayRequests();
                    if (dayRequests != null && !dayRequests.isEmpty()) {
                        Flux<OccupancyDayEntity> occupancyDayEntitiesToSave = Flux.fromIterable(dayRequests)
                                .map(dayRequest -> OccupancyDayEntity.builder()
                                        .occupancyId(savedOccupancyEntity.getId())
                                        .dayId(dayRequest.getDayId())
                                        .status(dayRequest.getStatus() != null ? dayRequest.getStatus() : Constants.ACTIVE)
                                        .createdAt(LocalDateTime.now())
                                        .createdBy(Constants.USER_ADMIN)
                                        .build());

                        return occupancyDayRepository.saveAll(occupancyDayEntitiesToSave)
                                .collectList()
                                .then(Mono.just(savedOccupancyEntity));
                    } else {
                        return Mono.just(savedOccupancyEntity);
                    }
                })
                .flatMap(finalOccupancyEntity ->
                        getOccupancByOccupancyAndDaysById(finalOccupancyEntity.getId())
                )
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<OccupancyByOccupancyAndDaysDetailDto> updateOccupancyByOccupancyAndDaysEntity(
            Integer occupancyId, OccupancyByOccupancyAndDaysRequest request) {
        return occupancyRepository.findById(occupancyId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Occupancy rule not found with ID: " + occupancyId)))
                .flatMap(occupancyEntity -> updateOccupancyEntity(occupancyEntity,request))
                .flatMap(occupancyEntityUpdate ->
                        updateOccupancyDayEntity(occupancyEntityUpdate.getId(),request)
                                .thenReturn(occupancyEntityUpdate))
                .flatMap(finalOccupancyEntity -> getOccupancByOccupancyAndDaysById(finalOccupancyEntity.getId()))
                .as(transactionalOperator::transactional);

    }

    private Mono<OccupancyEntity> updateOccupancyEntity(OccupancyEntity occupancyEntity, OccupancyByOccupancyAndDaysRequest request) {
        Optional.ofNullable(request.getRuleName())
                .ifPresent(occupancyEntity::setRuleName);
        Optional.ofNullable(request.getDescription())
                .ifPresent(occupancyEntity::setDescription);
        Optional.ofNullable(request.getCashPercentage())
                .ifPresent(occupancyEntity::setCashPercentage);
        Optional.ofNullable(request.getRewardsPercentage())
                .ifPresent(occupancyEntity::setRewardsPercentage);
        occupancyEntity.setUpdatedBy(Constants.USER_ADMIN);
        occupancyEntity.setUpdatedAt(LocalDateTime.now());
        return occupancyRepository.save(occupancyEntity);
    }

    private Mono<Void> updateOccupancyDayEntity(Integer occupancyId, OccupancyByOccupancyAndDaysRequest request) {
        List<OccupancyDayRequest> incomingDayRequests = request.getOccupancyDayRequests();
        if (incomingDayRequests == null || incomingDayRequests.isEmpty()) {
            return Mono.empty();
        }
        Flux<Void> updateOperations = Flux.fromIterable(incomingDayRequests)
                .flatMap(dayRequest -> occupancyDayRepository.updateStatusByOccupancyIdAndDayId(
                        occupancyId,
                        dayRequest.getDayId(),
                        dayRequest.getStatus(),
                        Constants.USER_ADMIN
                ));
        return updateOperations.collectList().then();
    }

    @Override
    public Mono<Void> deleteOccupancyByOccupancyAndDaysEntity(Integer occupancyId) {
        return occupancyRepository.deleteById(occupancyId);
    }

    @Override
    public Mono<OcupancyByRangesResponse> getListByRangesWithPagination(String startDate, String endDate, Integer size, Integer page) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        Integer offset = page * size;

        Mono<Integer> totalMono = occupancyByRangeRepository.countListOccupancyByRanges(startDate, endDate);
        Mono<List<OccupancyByRangeEntity>> dataMono = occupancyByRangeRepository.getListByRanges(startDate, endDate, size, offset)
                .collectList();

        Integer finalSize = size;
        Integer finalPage = page;
        return Mono.zip(totalMono, dataMono)
                .map(tuple -> {
                    Integer total = tuple.getT1();
                    List<OccupancyByRangeEntity> data = tuple.getT2();
                    boolean result = !data.isEmpty();
                    return OcupancyByRangesResponse.builder()
                            .result(result)
                            .total(total)
                            .data(data)
                            .size(finalSize)
                            .page(finalPage)
                            .build();
                })
                .switchIfEmpty(Mono.just(OcupancyByRangesResponse.builder()
                        .result(false)
                        .total(0)
                        .data(List.of())
                        .size(size)
                        .page(page)
                        .build()));
    }

    @Override
    public Mono<OccupancyByRangeEntity> getByRangesById(Integer id) {
        return occupancyByRangeRepository.findById(id);
    }

    @Override
    public Mono<OccupancyByRangeEntity> createOccupancyByRangeEntity(OccupancyByRangesRequest request) {
        var occupancyByRangeEntity = OccupancyByRangeEntity.builder()
                .hasDateRange(request.getHasDateRange())
                .rangeFromDate(request.getRangeFromDate())
                .rangeToDate(request.getRangeToDate())
                .hasTimeRange(request.getHasTimeRange())
                .rangeFromHour(request.getRangeFromHour())
                .rangeFromMinute(request.getRangeFromMinute())
                .maxRewardsPercentage(request.getMaxRewardsPercentage())
                .exceptionRewardsPercentage(request.getExceptionRewardsPercentage())
                .newMaxRewardsPercentage(request.getNewMaxRewardsPercentage())
                .status(Constants.ACTIVE)
                .createdAt(LocalDateTime.now())
                .createdBy(Constants.USER_ADMIN)
                .build();
        return occupancyByRangeRepository.save(occupancyByRangeEntity)
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<OccupancyByRangeEntity> updateOccupancyByRangeEntity(Integer id, OccupancyByRangesRequest request) {
        return occupancyByRangeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Occupancy By range not found with ID: " + id)))
                .flatMap(occupancyByRangeEntity -> updateEntity(occupancyByRangeEntity,request))
                .as(transactionalOperator::transactional);
    }

    private Mono<OccupancyByRangeEntity> updateEntity(OccupancyByRangeEntity occupancyByRangeEntity, OccupancyByRangesRequest request) {
        Optional.ofNullable(request.getHasDateRange())
                .ifPresent(occupancyByRangeEntity::setHasDateRange);
        Optional.ofNullable(request.getRangeFromDate())
                .ifPresent(occupancyByRangeEntity::setRangeFromDate);
        Optional.ofNullable(request.getRangeToDate())
                .ifPresent(occupancyByRangeEntity::setRangeToDate);
        Optional.ofNullable(request.getHasTimeRange())
                .ifPresent(occupancyByRangeEntity::setHasTimeRange);
        Optional.ofNullable(request.getRangeFromHour())
                .ifPresent(occupancyByRangeEntity::setRangeFromHour);
        Optional.ofNullable(request.getRangeFromMinute())
                .ifPresent(occupancyByRangeEntity::setRangeFromMinute);
        Optional.ofNullable(request.getMaxRewardsPercentage())
                .ifPresent(occupancyByRangeEntity::setMaxRewardsPercentage);
        Optional.ofNullable(request.getExceptionRewardsPercentage())
                .ifPresent(occupancyByRangeEntity::setExceptionRewardsPercentage);
        Optional.ofNullable(request.getNewMaxRewardsPercentage())
                .ifPresent(occupancyByRangeEntity::setNewMaxRewardsPercentage);

        occupancyByRangeEntity.setUpdatedBy(Constants.USER_ADMIN);
        occupancyByRangeEntity.setUpdatedAt(LocalDateTime.now());

        return occupancyByRangeRepository.save(occupancyByRangeEntity);
    }

    @Override
    public Mono<Void> deleteOccupancyByRangeEntity(Integer id) {
        return occupancyByRangeRepository.deleteById(id);
    }

    @Override
    public Mono<StandByRulesResponse> getListStandByRulesWithPagination(String searchTerm, Integer size, Integer page) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        Integer offset = page * size;

        Mono<Integer> totalMono = standbyRuleRepository.countListStandByRules(searchTerm);
        Mono<List<StandByRuleDto>> dataMono = standbyRuleRepository.getListStandByRules(searchTerm, size, offset)
                .collectList();

        Integer finalSize = size;
        Integer finalPage = page;
        return Mono.zip(totalMono, dataMono)
                .map(tuple -> {
                    Integer total = tuple.getT1();
                    List<StandByRuleDto> data = tuple.getT2();
                    boolean result = !data.isEmpty();
                    return StandByRulesResponse.builder()
                            .result(result)
                            .total(total)
                            .data(data)
                            .size(finalSize)
                            .page(finalPage)
                            .build();
                })
                .switchIfEmpty(Mono.just(StandByRulesResponse.builder()
                        .result(false)
                        .total(0)
                        .data(List.of())
                        .size(size)
                        .page(page)
                        .build()));
    }

    @Override
    public Mono<StandByRuleDetailDto> getStandByRuleById(Integer id) {
        return standbyRuleRepository.getStandByRuleDetail(id);
    }

    @Override
    public Flux<DropDownReservationTimeTypeResponse> getDropDownReservationTime(String searchTerm) {
        return reservationTimeTypeRepository.getDropDownReservationTime(searchTerm);
    }

    @Override
    public Flux<DropDownVisibilityTypeResponse> getDropDownVisivility(String searchTerm) {
        return visibilityTypeRepository.getDropDownVisivility(searchTerm);
    }

    @Override
    public Mono<StandbyRuleEntity> createStandByRuleEntity(StandByRulesRequest request) {
        var standbyRuleEntity = StandbyRuleEntity.builder()
                .idReservationTimeType(request.getIdReservationTimeType())
                .idVisibilityType(request.getIdVisibilityType())
                .status(Constants.ACTIVE)
                .createdAt(LocalDateTime.now())
                .createdBy(Constants.USER_ADMIN)
                .build();
        return standbyRuleRepository.save(standbyRuleEntity)
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<StandbyRuleEntity> updateStandByRuleEntity(Integer id, StandByRulesRequest request) {
        return standbyRuleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "StandByRule entity not found with ID: " + id)))
                .flatMap(standbyRuleEntity -> updateEntityStandByRule(standbyRuleEntity,request))
                .flatMap(standbyRuleEntityUpdate ->
                        updateReservationTimeTypeEntity(standbyRuleEntityUpdate,request)
                                .thenReturn(standbyRuleEntityUpdate))
                .as(transactionalOperator::transactional);
    }

    private Mono<StandbyRuleEntity> updateEntityStandByRule(StandbyRuleEntity standbyRuleEntity,StandByRulesRequest request) {
        Optional.ofNullable(request.getIdReservationTimeType())
                .ifPresent(standbyRuleEntity::setIdReservationTimeType);
        Optional.ofNullable(request.getIdVisibilityType())
                .ifPresent(standbyRuleEntity::setIdVisibilityType);
        standbyRuleEntity.setUpdatedBy(Constants.USER_ADMIN);
        standbyRuleEntity.setUpdatedAt(LocalDateTime.now());
        return standbyRuleRepository.save(standbyRuleEntity);
    }

    private Mono<Void> updateReservationTimeTypeEntity(StandbyRuleEntity standbyRuleEntityUpdate, StandByRulesRequest request) {
        return reservationTimeTypeRepository.findById(standbyRuleEntityUpdate.getIdReservationTimeType())
                .flatMap(reservationTimeTypeEntity -> {
                    Optional.ofNullable(request.getStandbyHours())
                            .ifPresent(reservationTimeTypeEntity::setStandbyHours);
                    reservationTimeTypeEntity.setUpdatedBy(Constants.USER_ADMIN);
                    reservationTimeTypeEntity.setUpdatedAt(LocalDateTime.now());
                    return reservationTimeTypeRepository.save(reservationTimeTypeEntity);
                })
                .then();
    }

    @Override
    public Mono<Void> deleteStandByRuleEntity(Integer id) {
        return standbyRuleRepository.deleteById(id);
    }
}

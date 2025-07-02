package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.request.OccupancyByOccupancyAndDaysRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyByOccupancyAndDaysDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyByOccupancyAndDaysResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.request.OccupancyByRangesRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.response.OcupancyByRangesResponse;
import com.proriberaapp.ribera.Domain.entities.OccupancyByRangeEntity;
import reactor.core.publisher.Mono;

public interface OccupancyConfigurationService {

    Mono<OccupancyByOccupancyAndDaysResponse> getListByOccupancyAndDaysWithPagination(String searchTerm, Integer size, Integer page);

    Mono<OccupancyByOccupancyAndDaysDetailDto> getOccupancByOccupancyAndDaysById(Integer occupancyId);

    Mono<OccupancyByOccupancyAndDaysDetailDto> createOccupancyByOccupancyAndDaysEntity(OccupancyByOccupancyAndDaysRequest request);

    Mono<OccupancyByOccupancyAndDaysDetailDto> updateOccupancyByOccupancyAndDaysEntity(Integer occupancyId,OccupancyByOccupancyAndDaysRequest request);

    Mono<Void> deleteOccupancyByOccupancyAndDaysEntity(Integer occupancyId);

    Mono<OcupancyByRangesResponse> getListByRangesWithPagination(String startDate, String endDate, Integer size,  Integer page);

    Mono<OccupancyByRangeEntity> getByRangesById(Integer id);

    Mono<OccupancyByRangeEntity> createOccupancyByRangeEntity(OccupancyByRangesRequest request);

    Mono<OccupancyByRangeEntity> updateOccupancyByRangeEntity(Integer id,OccupancyByRangesRequest request);

    Mono<Void> deleteOccupancyByRangeEntity(Integer id);
}

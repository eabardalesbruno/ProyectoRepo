package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.request.OccupancyByOccupancyAndDaysRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyByOccupancyAndDaysDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response.OccupancyByOccupancyAndDaysResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.request.OccupancyByRangesRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.response.OcupancyByRangesResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.request.StandByRulesRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.*;
import com.proriberaapp.ribera.Domain.entities.OccupancyByRangeEntity;
import com.proriberaapp.ribera.Domain.entities.StandbyRuleEntity;
import com.proriberaapp.ribera.services.admin.OccupancyConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.api}/occupancy-configuration")
@RequiredArgsConstructor
public class OccupancyConfigurationController {

    private final OccupancyConfigurationService occupancyConfigurationService;

    @GetMapping("/by-occupancy-and-days/with-pagination")
    public Mono<OccupancyByOccupancyAndDaysResponse>getOccupancyByOccupancyAndDaysWithPagination(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page
    ){
        return occupancyConfigurationService.getListByOccupancyAndDaysWithPagination(searchTerm,size,page);
    }

    @GetMapping("/by-occupancy-and-days/{occupancyId}")
    public Mono<OccupancyByOccupancyAndDaysDetailDto>getOccupancyByOccupancyAndDaysById(@PathVariable Integer occupancyId){
        return occupancyConfigurationService.getOccupancByOccupancyAndDaysById(occupancyId);
    }

    @PostMapping("/by-occupancy-and-days")
    public Mono<OccupancyByOccupancyAndDaysDetailDto> createOccupancyByOccupancyAndDays(
            @RequestBody OccupancyByOccupancyAndDaysRequest request){
        return occupancyConfigurationService.createOccupancyByOccupancyAndDaysEntity(request);
    }

    @PatchMapping("/by-occupancy-and-days/{occupancyId}")
    public Mono<OccupancyByOccupancyAndDaysDetailDto> updateOccupancyByOccupancyAndDays(
            @PathVariable Integer occupancyId,@RequestBody OccupancyByOccupancyAndDaysRequest request){
        return occupancyConfigurationService.updateOccupancyByOccupancyAndDaysEntity(occupancyId,request);
    }

    @DeleteMapping("/by-occupancy-and-days/{occupancyId}")
    public Mono<Void> deleteOccupancyByOccupancyAndDays(@PathVariable Integer occupancyId){
        return occupancyConfigurationService.deleteOccupancyByOccupancyAndDaysEntity(occupancyId);
    }

    @GetMapping("/by-ranges/with-pagination")
    public Mono<OcupancyByRangesResponse>getOccupancyByRangesWithPagination(
            @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "10") Integer size,@RequestParam(defaultValue = "0") Integer page){
        return occupancyConfigurationService.getListByRangesWithPagination(startDate, endDate, size, page);
    }

    @GetMapping("/by-ranges/{id}")
    public Mono<OccupancyByRangeEntity>getOccupancyByRangesById(@PathVariable Integer id){
        return occupancyConfigurationService.getByRangesById(id);
    }

    @PostMapping("/by-ranges")
    public Mono<OccupancyByRangeEntity> createOccupancyByRange(@RequestBody OccupancyByRangesRequest request){
        return occupancyConfigurationService.createOccupancyByRangeEntity(request);
    }

    @PatchMapping("/by-ranges/{id}")
    public Mono<OccupancyByRangeEntity> updateOccupancyByRanges(
            @PathVariable Integer id, @RequestBody OccupancyByRangesRequest requestUpdate){
        return occupancyConfigurationService.updateOccupancyByRangeEntity(id, requestUpdate);
    }

    @DeleteMapping("/by-ranges/{id}")
    public Mono<Void> deleteOccupancyByRanges(@PathVariable Integer id){
        return occupancyConfigurationService.deleteOccupancyByRangeEntity(id);
    }

    @GetMapping("/standby-rules/with-pagination")
    public Mono<StandByRulesResponse>getStandByRulesWithPagination(
            @RequestParam(required = false) String searchTerm, @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page){
        return occupancyConfigurationService.getListStandByRulesWithPagination(searchTerm, size, page);
    }

    @GetMapping("/standby-rules/by-user")
    public Mono<StandByRuleByUserResponse>getStandByRuleByUser(
            @RequestParam Integer bookingId,
            @RequestParam Boolean isUserInclub){
        return occupancyConfigurationService.getStandByRuleByUser(bookingId,isUserInclub);
    }

    @GetMapping("/standby-rules/{standByRuleId}")
    public Mono<StandByRuleDetailDto>getStandByRulesById(@PathVariable Integer standByRuleId){
        return occupancyConfigurationService.getStandByRuleById(standByRuleId);
    }

    @GetMapping("/standby-rules/drop-down-reservation-time-type")
    public Flux<DropDownReservationTimeTypeResponse> getDropDownReservationTime(
            @RequestParam(required = false) String searchTerm){
        return occupancyConfigurationService.getDropDownReservationTime(searchTerm);
    }

    @GetMapping("/standby-rules/drop-down-visibility-type")
    public Flux<DropDownVisibilityTypeResponse> getDropDownVisibility(
            @RequestParam(required = false) String searchTerm){
        return occupancyConfigurationService.getDropDownVisivility(searchTerm);
    }

    @PostMapping("/standby-rules")
    public Mono<StandbyRuleEntity> createStandByRules(@RequestBody StandByRulesRequest request){
        return occupancyConfigurationService.createStandByRuleEntity(request);
    }

    @PatchMapping("/standby-rules/{standByRuleId}")
    public Mono<StandbyRuleEntity>updateStandByRule(
            @PathVariable Integer standByRuleId, @RequestBody StandByRulesRequest requestUpdate){
        return occupancyConfigurationService.updateStandByRuleEntity(standByRuleId, requestUpdate);
    }

    @DeleteMapping("/standby-rules/{standByRuleId}")
    public Mono<Void> deleteStandByRules(@PathVariable Integer standByRuleId){
        return occupancyConfigurationService.deleteStandByRuleEntity(standByRuleId);
    }
}

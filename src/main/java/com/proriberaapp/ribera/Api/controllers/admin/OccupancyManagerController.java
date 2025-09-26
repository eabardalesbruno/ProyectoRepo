package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.OccupancyManagerCountDto;
import com.proriberaapp.ribera.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager")
@RequiredArgsConstructor
@Slf4j

public class OccupancyManagerController {

    private final UserClientService userClientService;

    @GetMapping("/occupancy/count")
    @ResponseStatus(HttpStatus.OK)
    public Flux<OccupancyManagerCountDto> getOccupancyManagerCountByBookingIds(
            @RequestParam List<Integer> bookingIds) {

        log.info("Fetching occupancy manager count for booking IDs: {}", bookingIds);
        return userClientService.getOccupancyManagerCountByBookingIds(bookingIds);
    }
}
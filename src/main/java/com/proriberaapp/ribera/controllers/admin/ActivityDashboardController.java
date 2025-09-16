package com.proriberaapp.ribera.controllers.admin;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Domain.dto.activity.response.ActivityDashboardResponseDTO;
import com.proriberaapp.ribera.application.service.ActivityDashboardService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/activity/dashboard")
@RequiredArgsConstructor
public class ActivityDashboardController {
    private final ActivityDashboardService activityDashboardService;

    @GetMapping
    public Mono<ActivityDashboardResponseDTO> getActivityDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return activityDashboardService.getActivityDashboard(date, page, size);
    }
}

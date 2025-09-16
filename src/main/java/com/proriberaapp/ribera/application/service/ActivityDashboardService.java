package com.proriberaapp.ribera.application.service;

import java.time.LocalDateTime;

import com.proriberaapp.ribera.Domain.dto.activity.response.ActivityDashboardResponseDTO;

import reactor.core.publisher.Mono;

public interface ActivityDashboardService {
    Mono<ActivityDashboardResponseDTO> getActivityDashboard(LocalDateTime date, int page, int size);
}

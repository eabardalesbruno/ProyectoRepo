package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.request.RequestReleaseLogDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response.RewardReleaseLogResponseDto;
import reactor.core.publisher.Mono;

public interface BoRewardsService {
    Mono<RewardReleaseLogResponseDto> createReleaseLog(RequestReleaseLogDto request);
}

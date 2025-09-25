package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.request.RequestReleaseLogDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response.RewardReleaseLogResponseDto;
import com.proriberaapp.ribera.services.admin.BoRewardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.api}/bo-rewards")
@RequiredArgsConstructor
public class BoRewardsController {

    private final BoRewardsService boRewardsService;

    @PostMapping("/reward-release-log")
    public Mono<RewardReleaseLogResponseDto> createRewardReleaseLog(@RequestBody RequestReleaseLogDto request){
       return  boRewardsService.createReleaseLog(request);
    }
}

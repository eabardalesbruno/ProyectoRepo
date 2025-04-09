package com.proriberaapp.ribera.Api.controllers.client;


import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import com.proriberaapp.ribera.services.client.UserRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-rewards")
public class UserRewardController {

    private final UserRewardService userRewardService;

    @GetMapping
    public Flux<UserRewardResponse> getAllRewards() {
        return userRewardService.findAll();
    }

    @GetMapping("/user/{userId}")
    public Flux<UserRewardResponse> getRewardsByUserId(@PathVariable Long userId) {
        return userRewardService.findByUserId(userId);
    }

    @GetMapping("/type/{type}")
    public Flux<UserRewardResponse> getRewardsByType(@PathVariable String type) {
        return userRewardService.findByType(RewardType.valueOf(type));
    }

}
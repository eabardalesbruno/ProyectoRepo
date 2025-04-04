package com.proriberaapp.ribera.services.client;

import java.util.List;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.SubscriptionFamilyResponse;
import reactor.core.publisher.Mono;

public interface MembershipsService {
    Mono<List<MembershipDto>> loadMembershipsActives(String userName);

    Mono<List<MembershipDto>> loadAllMemberships(int userId);

    Mono<List<MembershipDto>> loadMembershipsInsortInclub(String username);

    Mono<MembershipDto> getPromotionalGuestById(Integer id);

    Mono<List<MembershipDto>> loadMembershipsInsortInclubv1(String username, int userId, String token);



    Mono<List<SubscriptionFamilyResponse>> loadAllFamilies(String username, String tokenBackOffice);
}

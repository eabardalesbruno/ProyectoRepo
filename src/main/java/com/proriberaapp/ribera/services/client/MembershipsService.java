package com.proriberaapp.ribera.services.client;

import java.util.List;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;

import reactor.core.publisher.Mono;

public interface MembershipsService {
    Mono<List<MembershipDto>> loadMembershipsActives(String userName);

    Mono<List<MembershipDto>> loadAllMemberships(int userId);

    Mono<List<MembershipDto>> loadMembershipsInsortInclub(String username);
    

}

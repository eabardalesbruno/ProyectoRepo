package com.proriberaapp.ribera.services.point.user;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointDataResponse;
import reactor.core.publisher.Mono;

public interface UserPointService {
    Mono<UserPointDataResponse> getUserPoints(String username, Integer idMembershipFamily, String tokenBackOffice);
}

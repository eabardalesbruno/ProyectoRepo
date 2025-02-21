package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.MembershipsService;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partner-ribera")
@RequiredArgsConstructor
public class PartnerRiberaController {
    @Autowired
    private MembershipsService membershipsService;
    @Autowired
    private JwtProvider jtp;

    @GetMapping("/memberships")
    public Mono<?> getMemberships(@RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return this.membershipsService.loadAllMemberships(userClientId);
    }

    @GetMapping("/memberships/active")
    public Mono<List<MembershipDto>> getActiveMemberships(@RequestHeader("Authorization") String token) {
        String username = jtp.getUsernameFromToken(token);
        return this.membershipsService.loadMembershipsActives(username);
    }


}

package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.SubscriptionFamilyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.MembershipsService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partner")
@RequiredArgsConstructor
public class PartnerRiberaController {
    @Autowired
    private MembershipsService membershipsService;
    @Autowired
    private JwtProvider jtp;

    @GetMapping("/subscriptions/families")
    public Mono<ResponseEntity<List<SubscriptionFamilyResponse>>> getFamilies(
            @RequestHeader("Authorization") String token,
            @RequestHeader(value = "key-inclub-ribera", defaultValue = "") String tokenBackOffice) {
        String username = jtp.getUsernameFromToken(token);
        return membershipsService.loadAllFamilies(username, tokenBackOffice)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("/subscriptions/families/{username}")
    public Mono<ResponseEntity<List<SubscriptionFamilyResponse>>> getFamiliesByUsername(
            @PathVariable String username,
            @RequestHeader(value = "key-inclub-ribera", defaultValue = "") String tokenBackOffice) {
        return membershipsService.loadAllFamilies(username, tokenBackOffice)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

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

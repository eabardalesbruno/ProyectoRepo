package com.proriberaapp.ribera.Api.controllers.client;

import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.MembershipsService;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/partner-ribera")
public class PartnerRiberaController {
    @Autowired
    private MembershipsService membershipsService;
    @Autowired
    private  JwtProvider jtp;
    @GetMapping("/memberships")
    public Mono<?> getMemberships(@RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return this.membershipsService.loadAllMemberships(userClientId);
    }

}

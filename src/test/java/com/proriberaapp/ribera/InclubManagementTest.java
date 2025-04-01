package com.proriberaapp.ribera;

import com.proriberaapp.ribera.Api.controllers.exception.TokenInvalidException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.Api.controllers.exception.CredentialsInvalidException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.LoginInclubService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class InclubManagementTest {
    private int idUsuarioVitaliciaRibera = 151;
    private int idUsuarioSinVitalicia = 148;
    @Autowired
    private LoginInclubService loginInclubService;

    @Autowired
    private VerifiedDiscountService membershipValidateDiscountService;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void testLogin() throws TokenInvalidException {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321 23"))
                .verifyComplete();
    }

    @Test
    void testLoginSuccess() throws TokenInvalidException {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321#"))
                .expectNextMatches(responseLogin -> !responseLogin.getValue().isBlank()).verifyComplete();
    }

    @Test
    void testLoginFail() throws TokenInvalidException {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321 23"))
                .expectError(CredentialsInvalidException.class).verify();
    }

    @Test
    void testValidateToken() {
        StepVerifier.create(Mono.just(jwtProvider.validateToken(
                "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJFVjQ4NDI3MjgzIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IjEifV0sImlkIjoxNjgsImlhdCI6MTczMjgwOTEyMywiZXhwIjoxNzMyODQ1MTIzfQ.L3Cg4gMBlYYJN_23jfno-DDKyFfuMcJirVKCvBb4Y3JmAkbop-301_oXimlBFdF3")))
                .expectNextMatches(isValid -> isValid).verifyComplete();
    }

    @Test
    void testInValidateToken() {
        StepVerifier.create(Mono.just(jwtProvider.validateToken(
                "212eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJFVjQ4NDI3MjgzIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IjEifV0sImlkIjoxNjgsImlhdCI6MTczMjgwOTEyMywiZXhwIjoxNzMyODQ1MTIzfQ.L3Cg4gMBlYYJN_23jfno-DDKyFfuMcJirVKCvBb4Y3JmAkbop-301_oXimlBFdF3")))
                .expectNextMatches(isValid -> !isValid).verifyComplete();
    }

    @Test
    void testLoginFailWithPassword() throws TokenInvalidException {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321 23"))
                .expectError(CredentialsInvalidException.class).verify();
    }

    @Test
    void verifiedCredentialsInclub() {
        StepVerifier.create(loginInclubService.verifiedCredentialsInclub("EV48427283", "password321#"))
                .expectNextMatches(responseValidateCredential -> {
                    return responseValidateCredential.isData();
                })
                .verifyComplete();
    }

    /*
     * @Test
     * void validateSubscriptions() {
     * StepVerifier.create(VerifiedDiscountService.loadMemberships(
     * 12892))
     * .expectNextMatches(responseDataMembershipDto -> {
     * System.out.println(responseDataMembershipDto);
     * return !responseDataMembershipDto.isEmpty();
     * }).verifyComplete();
     * }
     */

/*     @Test
    void validateDiscountVitaliciaRibera() {
        StepVerifier.create(membershipValidateDiscountService.verifiedPercentajeDiscount(
                161))
                .expectNextMatches(discount -> {
                    return discount == 30;
                }).verifyComplete();
    }

    @Test
    void validateNotDiscount() {
        StepVerifier.create(membershipValidateDiscountService.verifiedPercentajeDiscount(
                148))
                .expectNextMatches(discount -> discount == 0).verifyComplete();
    } */
}

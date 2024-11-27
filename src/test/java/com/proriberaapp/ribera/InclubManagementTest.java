package com.proriberaapp.ribera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.Api.controllers.exception.CredentialsInvalidException;
import com.proriberaapp.ribera.services.client.LoginInclubService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.test.StepVerifier;

@SpringBootTest
public class InclubManagementTest {
    private int idUsuarioVitaliciaRibera = 151;
    private int idUsuarioSinVitalicia = 148;
    @Autowired
    private LoginInclubService loginInclubService;

    @Autowired
    private VerifiedDiscountService membershipValidateDiscountService;

    @Test
    void testLogin() {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321 23"))
                .verifyComplete();
    }

    @Test
    void testLoginSuccess() {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321#"))
                .expectNextMatches(responseLogin -> !responseLogin.isBlank()).verifyComplete();
    }

    @Test
    void testLoginFail() {
        StepVerifier.create(loginInclubService.login("EV48427283", "password321 23"))
                .expectError(CredentialsInvalidException.class).verify();
    }

    @Test
    void testLoginFailWithPassword() {
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

    @Test
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
    }
}

package com.proriberaapp.ribera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.services.client.LoginInclubService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.test.StepVerifier;

@SpringBootTest
public class InclubManagementTest {
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
    void verifiedCredentialsInclub() {
        StepVerifier.create(loginInclubService.verifiedCredentialsInclub("EV48427283", "password321#"))
                .expectNextMatches(responseValidateCredential -> {
                    System.out.println(responseValidateCredential);
                    return responseValidateCredential.isData();
                })
                .verifyComplete();
    }

    /* @Test */
    /* void validateSubscriptions() { */
    /* StepVerifier.create(VerifiedDiscountService.loadMemberships( */
    /* 12892)) */
    /* .expectNextMatches(responseDataMembershipDto -> { */
    /* System.out.println(responseDataMembershipDto); */
    /* return !responseDataMembershipDto.isEmpty(); */
    /* }).verifyComplete(); */
    /* } */
}

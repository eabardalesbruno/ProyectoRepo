package com.proriberaapp.ribera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.services.client.RoomOfferService;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class FilterTest {
    @Autowired
    private RoomOfferService roomOfferService;

    void filtroPorFechas() {

    }

    void filtroPorNumeroAdultos() {

    }

    void filtroPorNumeroTotalPersonas() {

    }

    @Test
    void filtrarDepartamentosConMaximaCapacidad() {
        String categoryName = "DEPARTAMENTO";
        /*
         * Flux<ViewRoomOfferReturn> view =
         * roomOfferService.findFilteredV2(categoryName, null, null, 0, 3, 2, 2, 0,
         * null,
         * false);
         * StepVerifier.create(view).expectNextCount(7).verifyComplete();
         */
    }
}

package com.proriberaapp.ribera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.Infraestructure.exception.PasswordResetCodeExpiretTimeException;
import com.proriberaapp.ribera.Infraestructure.exception.PasswordResetCodeNotFoundException;
import com.proriberaapp.ribera.Infraestructure.exception.PasswordResetCodeUsedException;
import com.proriberaapp.ribera.services.client.PasswordResetCodeService;

import reactor.test.StepVerifier;

@SpringBootTest
public class PasswordResetCodeTest {
    @Autowired
    private PasswordResetCodeService passwordResetCodeService;

    @Test
    void verfiedTimeExpired() {

        StepVerifier.create(this.passwordResetCodeService.verfiedCode("sddd"))
                .expectError(PasswordResetCodeExpiretTimeException.class)
                .verify();
    }
    @Test
    void save() {

        StepVerifier.create(this.passwordResetCodeService.generateResetCode("client", 1))
                .expectNextMatches(d -> {
                    return d.length() == 8;
                }).verifyComplete();
    }

    @Test
    void verfiedTimeExpiredHasNotExpired() {
        StepVerifier.create(this.passwordResetCodeService.verfiedCode("56067333")).expectNextMatches(d -> {
            return d.getReset_code().equals("56067333");
        }).verifyComplete();
    }

    @Test
    void checkIfPasswordExists() {
        StepVerifier.create(this.passwordResetCodeService.verfiedCode("vvvv"))
                .expectError(PasswordResetCodeNotFoundException.class)
                .verify();
    }

    @Test
    void checkIfWasUsed() {
        StepVerifier.create(this.passwordResetCodeService.verfiedCode("b"))
                .expectError(PasswordResetCodeUsedException.class)
                .verify();
    }
}

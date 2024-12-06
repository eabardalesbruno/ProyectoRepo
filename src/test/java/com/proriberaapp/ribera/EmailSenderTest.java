package com.proriberaapp.ribera;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.ConfirmReserveBooking;
import com.proriberaapp.ribera.utils.emails.PaymentByBankTransfer;

import reactor.test.StepVerifier;

@SpringBootTest
public class EmailSenderTest {
    private String to = "antony.iatunga@gmail.com";
    private String subject = "Test Email";
    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        baseEmailReserve.addEmailHandler(new ConfirmReserveBooking(
                "Junio",
                "Junio",
                "10",
                "15",
                5,
                "Habitacion 1",
                "Antony Inga Atunga",
                "1"));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }

    @Test
    void testSendEmailPaymentByBankTransfer() {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        baseEmailReserve.addEmailHandler(new PaymentByBankTransfer(
                "Antony Inga Atunga",
                new BigDecimal(100.0)));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }
}

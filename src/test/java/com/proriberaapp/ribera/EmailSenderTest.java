package com.proriberaapp.ribera;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.BookingEmailDto;
import com.proriberaapp.ribera.utils.emails.ConfirmPaymentByBankTransferAndCardTemplateEmail;
import com.proriberaapp.ribera.utils.emails.ConfirmReserveBookingTemplateEmail;
import com.proriberaapp.ribera.utils.emails.PaymentByBankTransferTemplateEmail;

import com.proriberaapp.ribera.utils.emails.RejectedPaymentTemplateEmail;
import com.proriberaapp.ribera.utils.emails.UploadReceiptLaterTemplateEmail;
import com.proriberaapp.ribera.utils.emails.PayLaterTemplateEmail;

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
        baseEmailReserve.addEmailHandler(new ConfirmReserveBookingTemplateEmail(
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
        baseEmailReserve.addEmailHandler(new PaymentByBankTransferTemplateEmail(
                "Antony Inga Atunga",
                new BigDecimal(100.0)));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }

    @Test
    void testSendEmailPaymentByBankTransferAndCard() {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        BookingEmailDto bookingEmailDto = new BookingEmailDto(
                "Habitacion 1",
                "Antony Inga Atunga",
                "1",
                "10/06/2021",
                "15/06/2021",
                "12:00",
                "https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/apartments/feb6ae4a-75dd-4db9-8d31-a0ba9a462c22-resized_IMG_7568.jpg",
                5, "Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593");
        baseEmailReserve.addEmailHandler(new ConfirmPaymentByBankTransferAndCardTemplateEmail(
                "Antony Inga Atunga",
                bookingEmailDto));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }

    @Test
    void testSendEmailRejectPayment() {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        baseEmailReserve.addEmailHandler(new RejectedPaymentTemplateEmail(
                "Antony Inga Atunga",
                "Fondos insuficientes",
                "Habitacion 1"));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }

    @Test
    void testSendEmailPayLater() {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        baseEmailReserve.addEmailHandler(new PayLaterTemplateEmail(
                "Antony Inga Atunga",
                "Habitacion 1",
                "1"));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }

    @Test
    void testSendEmailUploadReceiptLater() {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        baseEmailReserve.addEmailHandler(new UploadReceiptLaterTemplateEmail(
                "Antony Inga Atunga",
                "OP -0183066"));
        String emailBody = baseEmailReserve.execute();
        StepVerifier.create(emailService.sendEmail(to, subject, emailBody)).verifyComplete();
    }

}

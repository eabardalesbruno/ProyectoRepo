package com.proriberaapp.ribera.utils.emails;

import java.math.BigDecimal;

public class PaymentByBankTransfer implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private BigDecimal amount;

    public PaymentByBankTransfer(String clientName, BigDecimal amount) {
        this.clientName = clientName;
        this.amount = amount;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        String body = """
                <p class="font-size">Hola, %clientName</p>
                <p class="font-size">
                Muchas gracias por tu reserva. Ya estas más cerca en disfrutar con nosotros
                </p>
                <p class="font-size">
                El total de tu estancia es de: <strong class="strong-text">S/. %amount</strong>
                </p>
                <p class="font-size">
                Para confirmar la reserva realizaremos la verificación de tu pago.
                </p>
                <p class="font-size">
                <strong class="strong-text">En menos de 48 hr recibirás un correo electrónico</strong> con la constancia de confirmación donde estará el detalle de tu reserva.
                </p>
                """;
        return body.replaceAll("%clientName", clientName).replace("%amount", String.valueOf(
                amount.doubleValue()));
    }

    @Override
    public String getStyles() {
        return """
                .font-size {
                 font-size: 16px;
                 }
                .strong-text {
                color:#384860;
                }
                                """;
    }

}

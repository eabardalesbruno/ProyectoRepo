package com.proriberaapp.ribera.utils.emails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;

public class UploadReceiptLaterTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private String code;
    private String bookingId;

    private String urlBaseFrontend;

    public UploadReceiptLaterTemplateEmail(String clientName, String code, String bookingId, String urlBaseFrontend) {
        this.clientName = clientName;
        this.code = code;
        this.bookingId = bookingId;
        this.urlBaseFrontend = urlBaseFrontend;

    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        System.out.println("UploadReceiptLaterTemplateEmail" + this.urlBaseFrontend);
        String toDay = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        String body = """
                  <p class="font-size">
                  Estimado, %clientName
                  Este es un recordatorio de que la siguiente reserva  %code  no cuenta con la evidencia o imagen del pago. Le agradecería  que nos envíen  la imagen del pago lo más ante posible a más tardar a las 11:59 PM de %toDay.
                  </p>
                      <p class="font-size">
                <a href="%urlBaseFrontend/payment-method/%bookingId?token=%code" class="button">Subir comprobante de pago</a>
                  </p>
                     <p class="font-size">
                   Si tienes alguna consulta o quieres agregar algún dato extra, envíanos tu consulta por correo o canal de whatsapp. Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a través de agente BCP, agencias o cualquier método de pago dentro de la plataforma usando este enlace:
                  <a href="%urlBaseFrontend/bookings/disponibles">
                    www.riberadelrio/reservas.com
                    </a>
                   </p>
                   """;
        return body.replaceAll("%clientName", clientName).replaceAll("%code", code).replace("%toDay", toDay).replaceAll(
                "%bookingId",
                bookingId).replaceAll("%urlBaseFrontend", urlBaseFrontend);
    }

    @Override
    public String getStyles() {
        return """
                .card{
                    width: 349px !important;
                    border-radius: 16px !important;
                    background-color: white !important;
                    border: 1px solid #F6F7FB !important;

                    }
                .center{
                    text-align: center;
                }
                .font-size{
                    font-size: 16px;
                }
                .button{
                    width: 230px !important;
                    }
                """;
    }
}

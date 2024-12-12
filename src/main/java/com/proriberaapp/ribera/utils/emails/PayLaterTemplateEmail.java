package com.proriberaapp.ribera.utils.emails;

public class PayLaterTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private String roomName;
    private String bookingId;
    private int paymentLimitHours;

    public PayLaterTemplateEmail(String clientName, String roomName, String bookingId) {
        this.clientName = clientName;
        this.roomName = roomName;
        this.bookingId = bookingId;
        this.paymentLimitHours = 4;

    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        String body = """
                 <p class="font-size">Hola %clientName, este correo es para recordarte de que mantiene un pago pendiente de su reserva de:
                <strong class="font-italic"> %roomName </strong>. Tienes plazo de realizar tu pago %paymentLimitHours horas. Pasando ese limite se anulara tu reserva.</p>
                 <div class="card">
                    <p class="center">
                        <img src="https://s3.us-east-2.amazonaws.com/backoffice.documents/email/access-alarms.png"/>
                        </p>

                    <p class="center">
                    <strong>Reserva en espera</strong>
                    </p>

                    <p class="center">
                    <a class="button" href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId">Pagar ahora</a>
                   </p>
                  </div>
                   <p class="font-size">
                 Si tienes alguna consulta o quieres agregar algún dato extra, envíanos tu consulta por correo o canal de whatsapp. Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a través de agente BCP, agencias o cualquier método de pago dentro de la plataforma usando este enlace:
                 <a href="https://www.cieneguillariberadelrio.com/bookings/disponibles">
                 www.riberadelrio/reservas.com
                 </a>
                 </p>
                 """;
        return body.replaceAll("%clientName", clientName).replace("%roomName", roomName).replace("%paymentLimitHours",
                String.valueOf(paymentLimitHours))
                .replace("%bookingId", bookingId);
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

                .button{
                    width: 199px !important;
                    }
                """;
    }
}

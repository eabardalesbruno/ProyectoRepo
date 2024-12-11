package com.proriberaapp.ribera.utils.emails;

public class RejectedPaymentTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private String roomName;
    private String reason;

    public RejectedPaymentTemplateEmail(String clientName, String reason, String roomName) {
        this.clientName = clientName;
        this.reason = reason;
        this.roomName = roomName;

    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        String body = """
                <p class="font-size">Hola, %clientName:</p>
                <p class="font-size">
                Verificamos tu pago para la reserva del <strong>%roomName</strong>. Lo sentimos, pero no hemos podido completar
                </p>
                <p class="font-size"> su pago en este momento. <strong>Motivo de rechazo: %reason.</strong>

                </p>
                <p  class="font-size">Por favor, inténtelo de nuevo. Gracias.</p>
                <p>
                Recuerde que puede realizar su nueva reserva haciendo clic en el botón o usando este enlace:
                <a href="https://cieneguillariberadelrio.com/bookings/disponibles">www.riberadelrio/reservas.com</a>
                </p>
                <p class="font-size">
                <a href="https://cieneguillariberadelrio.com/bookings/disponibles" class="button">Quiero reservar nuevamente</a>
                </p>
                """;
        return body.replaceAll("%clientName", clientName).replaceAll("%reason", reason).replaceAll("%roomName",
                roomName);
    }

    @Override
    public String getStyles() {
        return """
                .font-size {
                    font-size: 14px;
                    margin: 0;
                }
                .button{
                width:283px ! important;}
                """;
    }

}

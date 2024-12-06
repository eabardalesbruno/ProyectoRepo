package com.proriberaapp.ribera.utils.emails;

public class ConfirmReserveBooking implements EmailHandler {
    private EmailHandler nextHandler;
    private String monthInit;
    private String monthEnd;
    private String dayInit;
    private String dayEnd;
    private long dayInterval;
    private String roomName;
    private String clientName;
    private String bookingId;

    public ConfirmReserveBooking(
            String monthInit,
            String monthEnd,
            String dayInit,
            String dayEnd,
            long dayInterval,
            String roomName,
            String clientName,
            String bookingId) {
        this.monthInit = monthInit;
        this.monthEnd = monthEnd;
        this.dayInit = dayInit;
        this.dayEnd = dayEnd;
        this.dayInterval = dayInterval;
        this.roomName = roomName;
        this.clientName = clientName;
        this.bookingId = bookingId;

    }

    @Override
    public String execute() {
        String body = """
                <p>Estimado(a) %clientName</p>
                <p>Se completo exitosamente el registro de su reserva: <strong class="font-italic">%roomName</strong>. Por favor, no se olvide de pagar su reserva.
                 </p>
                <div class="card">
                    <p style="font-size: 1rem; font-weight: 600; font-family: 'Poppins', sans-serif; margin: 0; padding: 0; padding-top: 10px; padding-bottom: 20px">Los datos de tu reserva</p>
                <table class=table-layout>
                    <tr><td>
                    <img src="https://s3.us-east-2.amazonaws.com/backoffice.documents/email/calendario.png" alt="calendario"/>
                    Entrada</td><td>Salida</td></tr>
                    <tr><td><span style="font-size: 1.05rem; font-weight: 500;">%monthInit %dayInit</span></td>
                    <td><span style="font-size: 1.05rem; font-weight: 500;">%monthEnd %dayEnd</span></td>
                    </tr>
                </table>
                <p>Duracion de estancia: <br> <strong>%dayInterval días</strong></p>
                <p>Seleccion de reserva: <br> <strong>%roomName</strong></p>
                <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId" class="button">Pagar ahora</a>
                    </div>
                    <p>
                    Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a través de agente BCP, agencias o cualquier método de pago dentro de la plataforma usando este enlace:
                    <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId">www.riberadelrio/reservas.com</a>
                    </p>
                """;
        return body.replaceAll("%roomName", roomName).replaceAll("%clientName", clientName)
                .replace("%monthInit", monthInit).replace("%monthEnd", monthEnd)
                .replace("%dayInit", dayInit).replace("%dayEnd", dayEnd)
                .replace("%dayInterval", String.valueOf(dayInterval))
                .replaceAll("%bookingId", String.valueOf(bookingId));
    }

    @Override
    public String getStyles() {
        return """
                .card{
                    width: 333px;}
                .font-italic{
                    font-style: italic;
                }
                """;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

}

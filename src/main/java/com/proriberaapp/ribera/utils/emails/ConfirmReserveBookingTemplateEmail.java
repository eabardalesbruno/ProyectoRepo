package com.proriberaapp.ribera.utils.emails;

public class ConfirmReserveBookingTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String monthInit;
    private String monthEnd;
    private String dayInit;
    private String dayEnd;
    private long dayInterval;
    private String roomName;
    private String clientName;
    private String bookingId;
    private String totalPeoples;

    public ConfirmReserveBookingTemplateEmail(
            String monthInit,
            String monthEnd,
            String dayInit,
            String dayEnd,
            long dayInterval,
            String roomName,
            String clientName,
            String bookingId, String totalPeoples) {
        this.monthInit = monthInit;
        this.monthEnd = monthEnd;
        this.dayInit = dayInit;
        this.dayEnd = dayEnd;
        this.dayInterval = dayInterval;
        this.roomName = roomName;
        this.clientName = clientName;
        this.bookingId = bookingId;
        this.totalPeoples = totalPeoples;

    }

    @Override
    public String execute() {
        String body = """
                <p class="font-size">Estimado(a) %clientName</p>
                <p class="font-size">Se completo exitosamente el registro de su reserva: <strong class="font-italic">%roomName</strong>. Por favor, no se olvide de pagar su reserva.
                 </p>
                <div class="card">
                    <p style="font-size: 1rem; font-weight: 600; font-family: 'Poppins', sans-serif; margin: 0; padding: 0; padding-top: 10px; padding-bottom: 20px">Los datos de tu reserva</p>
                <table class=table-layout>
                <tbody>
                <tr>
                    <td>
                    <table>
                    <tbody>
                    <tr>
                    <td rowspan="3" style="padding-right:10px">
                    <img src="https://s3.us-east-2.amazonaws.com/backoffice.documents/email/calendario.png" alt="calendario"/>
                    </td>
                    </tr>

                    <tr>
                    <td>
                    Entrada
                    </td>
                    <td rowspan="3" class="border">
                    </td>
                    <td style="padding-left:20px">
                    Salida
                    </td>
                    </tr>
                    <tr>
                    <td>
                    %monthInit
                    <strong style="font-size:24px">%dayInit</strong>
                    </td>
                    <td style="padding-left:20px">
                    %monthEnd
                    <strong style="font-size:24px">%dayEnd</strong>
                    </td>

                    </tr>
                    </tbody>
                    </table>
                    <td>
                </tr>
                </tbody>
                </table>
                <p>Duracion de estancia: <br> <strong>%dayInterval noche</strong></p>
                <p>Seleccion de reserva: <br> <strong>%roomName
                    <br>
                    para %totalPeoples
                </strong></p>
                <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId" class="button">Pagar ahora</a>
                    </div>
                    <p class="font-size">
                    Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a través de agente BCP, agencias o cualquier método de pago dentro de la plataforma usando este enlace:
                    <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId">www.riberadelrio/reservas.com</a>
                    </p>
                """;
        return body.replaceAll("%roomName", roomName).replaceAll("%clientName", clientName)
                .replace("%monthInit", monthInit).replace("%monthEnd", monthEnd)
                .replace("%dayInit", dayInit).replace("%dayEnd", dayEnd)
                .replace("%dayInterval", String.valueOf(dayInterval))
                .replaceAll("%bookingId", String.valueOf(bookingId))
                .replace("%totalPeoples", String.valueOf(totalPeoples));
    }

    @Override
    public String getStyles() {
        return """
                .border{
                border-right: 2px;
                border-style: solid;
                padding-right: 10px;
                border-left: none;
                border-bottom: none;
                border-color:#E5E5E5;
                border-top: none;
                }
                .card{
                    width: 333px;
                    border-radius: 8px;
                    }

                """;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

}

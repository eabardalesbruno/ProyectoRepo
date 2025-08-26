package com.proriberaapp.ribera.utils.emails;

import lombok.Builder;

@Builder
public class NewChangeRoomBookingTemplateEmail implements EmailHandler {

    private EmailHandler nextHandler;
    private String imgUrlNew;
    private String clientName;
    private String roomNameOld;
    private String roomNameNew;
    private Integer bookingId;
    private String checkIn;
    private String checkOut;
    private String totalNights;
    private String totalPeople;
    private String approximateArrival;
    private String location;

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        return String.format("""
                    <div style="padding: 40px 140px; box-sizing: border-box;">
                        <h1 style="font-size: 28px; color: #1e1e1e; margin: 0;">¡%s, tu reserva ha sido cambiada!</h1>
                        <p style="font-size: 16px; margin-top: 20px; color: #384860;">Estimado(a),%s</p>
                        <p style="font-size: 16px; margin: 0; color: #384860;">El presente es para informar que se completó exitosamente el registro de su cambio para la reserva de: <b style="font-style: italic;">%s</b>.</p>
                        <p style="font-size: 16px; margin-top: 20px; font-weight: bold;">Información sobre el cambio de reserva</p>
                        <div class="card-reserve">
                            <table style="width: 100%%; border-collapse: collapse;">
                                <tr>
                                    <td style="width: 50%%;">
                                        <img src="%s" alt="Habitación" style="width: 100%%; display: block; border-top-left-radius: 8px; border-bottom-left-radius: 8px;">
                                    </td>
                                    <td style="width: 50%%; padding: 24px; vertical-align: top;">
                                        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #cbd5e0; padding-bottom: 12px; margin-bottom: 12px;">
                                            <h3 style="margin: 0; font-size: 20px;">%s</h3>
                                            <span style="background-color: #139041; color: #FFFFFF; padding: 4px 8px; border-radius: 999px; font-weight: 400; font-size: 14px;margin-left:5px;">Pagado</span>
                                        </div>
                                        <table style="width: 100%%; border-collapse: collapse;">
                                            <tbody>
                                                <tr>
                                                    <td colspan="2" style="padding: 4px 0; font-size: 14px; font-weight: 500; color: #666;">Titular de la reserva:</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="padding: 0 0 8px 0; font-size: 14px; font-weight: bold;">%s</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="padding: 4px 0; font-size: 14px; font-weight: 500; color: #666;">Código de reserva:</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="padding: 0 0 8px 0; font-size: 14px; font-weight: bold;">%d</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="padding: 16px 0 0 0;">
                                                        <table style="width: 100%%; border-collapse: collapse;">
                                                            <tbody>
                                                                <tr>
                                                                    <td style="width: 50%%; font-size: 14px; font-weight: 500; color: #666;">Check-in:</td>
                                                                    <td style="width: 50%%; font-size: 14px; font-weight: 500; color: #666;">Check-out:</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="font-weight: bold;">%s</td>
                                                                    <td style="font-weight: bold;">%s</td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="padding: 8px 0 0 0; font-size: 14px;">
                                                        Hora de llegada aproximada: <b>%s</b><br/>
                                                        <span style="font-style: italic; color: #025928;">(*) Recuerda que el check-in es a las 3:00 P.M.</span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 4px 0; font-size: 14px; font-weight: 500; color: #666;">Duración total de estancia:</td>
                                                    <td style="padding: 4px 0; text-align: right; font-size: 14px; font-weight: bold;">%s</td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 4px 0; font-size: 14px; font-weight: 500; color: #666;">Cantidad de personas:</td>
                                                    <td style="padding: 4px 0; text-align: right; font-size: 14px; font-weight: bold;">%s</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="padding: 8px 0 0 0; font-size: 14px; font-weight: 500; color: #666;">Ubicación:</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="font-weight: bold; font-size: 14px;">%s</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="footer-alert" style="margin-top: 24px;">
                            <h3 style="color: #6a6a6a; margin-top: 0; margin-bottom: 8px;">⚠️ Condiciones de cancelación o cambio de reserva</h3>
                            <p style="font-size: 14px; color: #6a6a6a; margin: 0;">Al realizar el pago de la reserva, cuenta con 15 días para solicitar un cambio de fecha. Pasado este plazo, no se realizará ningún reembolso en caso de cancelación. El cambio de alojamiento puede realizarse una vez que el usuario haya pagado la reserva, en los siguientes casos: cuando el alojamiento (habitación o departamento) se encuentre en mantenimiento; cuando el cliente desee modificar la fecha de alojamiento; o cuando haya reservado una habitación matrimonial y prefiera cambiarla por un departamento (en este último caso, se aplicará un cargo adicional). Si no te presentas, el cargo será igual al de cancelación. Para mayor información revisar nuestros <a href="#" style="color: #025928; text-decoration: none;">términos y condiciones</a>.</p>
                        </div>
                    </div>
                    <p style="margin-top: 40px; font-size: 14px; color: #6a6a6a;">Este correo es solo de carácter informativo, no es un comprobante de pago, en caso de no poder usar la reservación, por favor llamar con 2 días de anticipación.</p>
                    <p style="margin-top: 8px; font-size: 14px; color: #6a6a6a;">Muchas gracias.</p>
                """, clientName, clientName, roomNameOld, imgUrlNew, roomNameNew, clientName, bookingId, checkIn, checkOut, approximateArrival, totalNights, totalPeople, location);
    }

    @Override
    public String getStyles() {
        return """
                    .card-reserve {
                        border: 1px solid #BCBCBC;
                        background-color: white;
                        border-radius: 8px;
                        overflow: hidden; /* Esto asegura que el borde sea continuo con la imagen */
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                        padding:24px;
                    }
                    .footer-alert {
                        background-color: #FFFAF0;
                        padding: 16px;
                        border: 1px solid #EFBD0C;
                        border-radius: 4px;
                    }
                """;
    }
}

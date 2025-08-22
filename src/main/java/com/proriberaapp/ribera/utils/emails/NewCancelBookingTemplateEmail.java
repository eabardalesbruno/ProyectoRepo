package com.proriberaapp.ribera.utils.emails;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class NewCancelBookingTemplateEmail implements EmailHandler {

    private EmailHandler nextHandler;
    private String clientName;
    private String roomName;
    private BigDecimal additionalCost;
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
        String cancellationMessage;
        if (this.additionalCost!=null && this.additionalCost.compareTo(BigDecimal.ZERO) > 0) {
            cancellationMessage = String.format("Se te cobrará un cargo adicional por la cancelación de: <b>S/ %s</b>.", this.additionalCost.toString());
        } else {
            cancellationMessage = "¡Estás de suerte! tu cancelación es gratis.";
        }

        return String.format("""
                        <div style="font-family: Arial, sans-serif; padding: 40px 140px; color: #1a202c; background-color: #ffffff;">
                            <div style="padding-bottom: 24px;">
                                <h1 style="font-size: 28px; margin-bottom: 8px;">¡%s, tu reserva en Cieneguilla está cancelada!</h1>
                                <p style="font-size: 16px; margin: 0;">Hace poco nos pediste cancelar tu reserva del <b>%s</b>. %s</p>
                                <p style="font-size: 16px; margin-top: 40px; font-weight: bold;">Datos de la reserva</p>
                            </div>
                            <div style="border: 1px solid #BCBCBC; padding: 24px; border-radius: 16px;">
                                <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #cbd5e0; padding-bottom: 12px; margin-bottom: 12px;">
                                    <h3 style="margin: 0; font-size: 20px;">%s</h3>
                                    <span style="background-color: #fee2e2; color: #ef4444; padding: 4px 12px; border-radius: 9999px; font-weight: bold; font-size: 14px; margin-left:10px;">Cancelado</span>
                                </div>
                                <table style="width: 100%%; border-collapse: collapse;">
                                    <tbody>
                                        <tr>
                                            <td colspan="2" style="padding: 4px 0; font-size: 14px; font-weight: 500; color: #666;">Titular de la reserva:</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" style="padding-bottom: 8px; font-size: 14px; font-weight: bold;">%s</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" style="padding: 4px 0; font-size: 14px; font-weight: 500; color: #666;">Código de reserva:</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" style="padding-bottom: 8px; font-size: 14px; font-weight: bold;">%d</td>
                                        </tr>
                                        <tr style="border-top: 1px solid #D2D2D2;">
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
                                        <tr style="border-top: 1px solid #D2D2D2;">
                                            <td colspan="2" style="padding: 8px 0 0 0; font-size: 14px; font-weight: 500; color: #666;">Ubicación:</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" style="font-weight: bold; font-size: 14px;">%s</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div style="margin-top: 24px; font-size: 14px; color: #6b7280;">
                                <p>Recuerda que puedes realizar tu nueva reserva haciendo clic en el botón o usando este enlace:</p>
                                <a href="https://www.riberadelrio/reservas.com" style="color: #025928;">www.riberadelrio/reservas.com</a>
                            </div>
                        </div>
                        """,
                clientName, roomName, cancellationMessage, roomName, clientName, bookingId, checkIn, checkOut, approximateArrival, totalNights, totalPeople, location);
    }


    @Override
    public String getStyles() {
        return "";
    }
}

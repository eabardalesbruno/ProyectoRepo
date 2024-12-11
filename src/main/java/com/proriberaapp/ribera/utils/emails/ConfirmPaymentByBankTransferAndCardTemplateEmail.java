package com.proriberaapp.ribera.utils.emails;

public class ConfirmPaymentByBankTransferAndCardTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private BookingEmailDto bookingEmailDto;

    public ConfirmPaymentByBankTransferAndCardTemplateEmail(String clientName, BookingEmailDto bookingEmailDto) {
        this.clientName = clientName;
        this.bookingEmailDto = bookingEmailDto;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        String body = """
                        <p class="font"> Estimado(a), %clientName </p>
                <p class="font">El presente es para informar que se completo exitosamente el registro de su pago para la reserva de:
                <strong class="strong-text">%roomName</strong></p>
                <div class="card">
                    <table class="table-layout">
                    <tbody>
                         <tr>
                            <td width="433">
                            <table>
                            <tbody>
                            <tr>
                            <td width="433">
                            <img src="%imgSrc" width="433" class="img" alt="calendario"/>
                            </td>
                            </tr>
                            </tbody>
                            </table>
                            </td>
                            <td width="16"></td>

                             <td class="container-data">
                             <table width="100%" style="padding-top: 17px;
                padding-bottom: 17px;
                box-sizing: border-box;">
                                <tbody>
                                <tr>
                                <td>
                                <p class="room-name" ><strong>%roomName</strong></p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin">Titular de la reserva:</p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin"><strong>%titular</strong></p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin">Código de reserva:</p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin"><strong>%code</strong></p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <hr class="hr"/>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <table>
                                <tbody>
                                <tr>
                                <td>
                                <p class="no-margin">Checkin:</p>
                                <p class="no-margin"><strong>%dateCheckIn</strong></p>
                                </td>
                                <td width="180">
                                </td>
                                <td>
                                <p class="no-margin">Checkout:</p>
                                <p class="no-margin"><strong>%dateCheckOut</strong></p>
                                </td>
                                </tr>
                                </tbody>
                                </table>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin">Hora de llegada aproximada: %hourCheckIn </p>
                                <p class="check-in">(*) Recuerda que el check-in es las 3:00 P.M.</p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin">Duración total de estancia:<strong> %days <strong>días</p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <p class="no-margin">Cantidad de personas: <strong>4 adultos, 2 niños<strong></p>
                                </td>
                                </tr>
                                <tr>
                                <td>
                                <hr class="hr"/>
                                </td>
                                </tr>
                                <tr>

                                <td>
                                <p class="no-margin">Ubicación:</p>
                                <p class="no-margin"><strong>%location</strong></p>
                                </td>
                                </tr>

                                </tbody>
                             </table>
                            </tr>


                    </tbody>


                    </table>

                </div>
                   <p class="font">
                    Este correo es solo de carácter informativo, no es un comprobante de pago, en caso de no poder usar la reservación, por favor llamar con 2 días de anticipación.
                Muchas gracias.
                    </p>

                        """;
        return body.replaceAll("%clientName", clientName)
                .replace("%roomName", bookingEmailDto.getRoomName())
                .replace("%imgSrc", bookingEmailDto.getImgSrc())
                .replace("%titular", bookingEmailDto.getClientName())
                .replace("%code", bookingEmailDto.getCode())
                .replace("%dateCheckIn", bookingEmailDto.getDateCheckIn())
                .replace("%dateCheckOut", bookingEmailDto.getDateCheckOut())
                .replace("%hourCheckIn", bookingEmailDto.getHourCheckIn())
                .replace("%days", String.valueOf(bookingEmailDto.getDays()))
                .replace("%location", bookingEmailDto.getLocation());
    }

    @Override
    public String getStyles() {
        return """
                .check-in{
                margin:0;font-size:12px;
                color:#216D42;
                font-weight: 400;
                }
                .room-name{
                margin:0;font-size:20px

                }
                        p.no-margin{
                        margin: 0;
                        }
                        .container-data{
                            vertical-align: baseline;
                            font-size:14px;
                        }
                            .container-img{
                            width: 433px;
                            padding-right: 16px;
                            }
                           .container-img .img{
                                width: 433px;
                            }
                            .table-layout{
                                font-family: 'Product Sans', sans-serif;
                                width: 100%;
                            }
                            .hr{
                                border: 1px solid #E1E1E1;
                                margin:0;
                            }
                            .font {
                             font-size: 16px;
                                font-family: 'Product Sans', sans-serif;
                             }
                            .card{
                            width: 847px;
                            padding: 14px 24px;
                            }
                            .strong-text {
                            color:#384860;
                            font-style:italic;
                            }
                                            """;
    }

}

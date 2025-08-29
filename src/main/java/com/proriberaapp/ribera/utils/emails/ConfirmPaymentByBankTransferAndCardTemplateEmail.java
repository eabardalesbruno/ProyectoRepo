package com.proriberaapp.ribera.utils.emails;

public class ConfirmPaymentByBankTransferAndCardTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private BookingEmailDto bookingEmailDto;
    private boolean isAlimentation;

    public ConfirmPaymentByBankTransferAndCardTemplateEmail(String clientName, BookingEmailDto bookingEmailDto) {
        this.clientName = clientName;
        this.bookingEmailDto = bookingEmailDto;
        this.isAlimentation = false;
    }

    public ConfirmPaymentByBankTransferAndCardTemplateEmail(String clientName, BookingEmailDto bookingEmailDto,
            boolean isAlimentation) {
        this.clientName = clientName;
        this.bookingEmailDto = bookingEmailDto;
        this.isAlimentation = isAlimentation;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        String body = """
                <section class="container-body-email">
                      <h3 class="title">
                        ¡Gracias, %clientName! Tu reserva en Cieneguilla está confirmada.
                      </h3>
                      <p class="description">
                        Hola, %clientName, nos complace informarle que el registro de su pago para la reserva del <strong class="strong-text"> %roomName</strong>, se ha completado exitosamente.
                      </p>
                      <div class="container-reservation">
                        <h5>Datos de la reserva y pago</h5>
                        <div class="container-reservation-info">
                          <h4>%roomName</h4>
                          <div class="container-titular-reserva">
                            <p>
                              Titular de la reserva:<br />
                              <b>%clientName</b>
                            </p>
                            <p>
                              Código de reserva:<br />
                              <b>%bookingId</b>
                            </p>
                          </div>
                          <hr />
                          
                          <table class="container-info-reserva" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                              <tr>
                                <td colspan="3">
                                  <table class="container-check" cellpadding="0" cellspacing="0">
                                    <tbody>
                                      <tr>
                                        <td style="vertical-align: top; padding-right: 15px;"> <p>
                                            Check-in:<br />
                                            <b>%dayInit %monthInit</b>
                                          </p>
                                        </td>
                                        <td style="vertical-align: top;">
                                          <hr style="border: 1px solid #bcbcbc; height: 3rem; width: 1px; margin: 0; display: block;" /> </td>
                                        <td style="vertical-align: top; padding-left: 15px;"> <p>
                                            Check-out:<br />
                                            <b>%dayEnd %monthEnd</b>
                                          </p>
                                        </td>
                                      </tr>
                                    </tbody>
                                  </table>
                                </td>
                              </tr>
                              <tr>
                                <td colspan="3">
                                  <p>
                                    Hora de llegada aproximada: <b>10:00 A.M</b><br />
                                    <span>(*) Recuerda que el check-in es las 3:00 P.M.</span>
                                  </p>
                                  <p>Duración total de estancia: <b>%dayInterval noches</b></p>
                                  <p>Cantidad de personas: <b>%totalPeoples</b></p>
                                </td>
                              </tr>
                            </tbody>
                          </table>
                          <hr />
                          <div class="container-location">
                            <p>
                              Ubicación:<br />
                              <b
                                >Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico
                                Etapa III, Cercado de Lima 15593</b
                              >
                            </p>
                          </div>
                        </div>
                      </div>
                      
                      <div class="container-reservation-payment">
                      	<div>
                            <h3>Resumen de pago</h3> 
                        </div>
                        <div>
                            <div class="align-right">BCP</div>
                            <h4>Pago con descuento de socio</h4>
                        </div>
                        <div>
                          	<div class="align-right">S/154.33</div>
                          	<h4>30% Dscto: Socio de Ribera</h4>
                        </div>
                        <div>
                          	<div class="align-right">-S/44.15</div>
                          	<h4>20% Alimentación</h4>
                        </div>
                        <div class="total-container">
                          	<div class="align-right"><h3>S/110.18</h3></div>
                      		<h3>Total a pagar pago</h3>
                        </div>
                        <div class="container-reservation-warning">
                          <p><div class="icon-container">⚠️</div>
                          <strong style="padding-left: 2px;">Condiciones de cancelación o cambio de reserva</strong></p>
                          <p class="text-warning">Al realizar el pago de la reserva, dispone de <strong>15 días</strong> para solicitar un cambio de fecha; pasado este plazo no habrá reembolso por cancelación. El cambio de alojamiento se permitirá únicamente si el espacio reservado se encuentra en mantenimiento, si el cliente solicita modificar la fecha o si desea cambiar de habitación matrimonial a departamento (con cargo adicional). En caso de no presentarse, se aplicará el mismo cargo que por cancelación.</p>
                          <p class="text-warning">Para más detalles, consulte nuestros <a class="link-warning" href="https://cieneguillariberadelrio.com/terms-and-conditions" target="_blank"><strong>Términos y condiciones.</strong></a></p>
                        </div>
                      </div>
                      <div class="container-footer-info">
                        <p>
                          Si tienes alguna consulta o quieres agregar algún dato extra, envíanos tu
                          consulta por correo o canal de whatsapp. Recuerde que el pago lo puede
                          realizar mediante deposito en nuestra cuenta a través de agente BCP,
                          agencias o cualquier método de pago dentro de la plataforma usando este
                          enlace:
                          <a
                            href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId"
                            target="_blank"
                            >www.riberadelrio/reservas.com</a
                          >
                        </p>
                      </div>
                </section>
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
                .replace("%location", bookingEmailDto.getLocation())
                .replace("%cantidadPersonas", bookingEmailDto.getCantidadPersonas())
                .replace("%alimentation",
                        isAlimentation
                                ? "<tr><td><p class=\"no-margin    \"><strong>Con alimentación</strong></p></td></tr>"
                                : "");
    }

    @Override
    public String getStyles() {
        return """
                .container-body-email .title {
                  color: #1e1e1e;
                  font-family: Poppins, Arial, sans-serif;
                  font-weight: 600;
                  font-size: 24px;
                  max-width: 695px;
                }
                
                .container-body-email .description {
                  max-width: 695px;
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 400;
                  font-size: 16px;
                  color: #384860;
                }
                
                .container-body-email .description .description-bold {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-style: italic;
                  font-size: 16px;
                }
                
                .container-body-email .container-card p {
                  margin: 0;
                  font-family: Poppins, Arial, sans-serif;
                  font-weight: 600;
                  font-size: 16px;
                  text-align: center;
                  color: #1e1e1e;
                }
                
                .container-body-email .container-card a {
                  background: #025928;
                  border-radius: 24px;
                  padding: 8px 16px;
                  color: #fff;
                  text-decoration: none;
                  font-size: 14px;
                  display: inline-block;
                }
                
                .container-body-email .container-reservation {
                  margin-top: 2rem;
                }
                
                .container-body-email .container-reservation h5 {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-size: 16px;
                  color: #384860;
                }
                
                .container-body-email .container-reservation .container-reservation-info {
                  border: 1px solid #bcbcbc;
                  width: 695px;
                  border-radius: 16px;
                  padding: 24px;
                  background: #fff;
                }
                
                .container-body-email .container-reservation .container-reservation-info h4 {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-size: 20px;
                  color: #1e1e1e;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-titular-reserva p {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 400;
                  font-size: 14px;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-titular-reserva b {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-size: 14px;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-info-reserva .container-check hr {
                  border: 1px solid #bcbcbc;
                  height: 3rem;
                  width: 1px;
                  margin: 0;
                  display: block;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-info-reserva .container-check p {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 400;
                  font-size: 14px;
                  color: #1e1e1e;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-info-reserva .container-check p b {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-size: 14px;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-info-reserva p {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 400;
                  font-size: 14px;
                  color: #1e1e1e;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-info-reserva p span {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-size: 14px;
                  color: #025928;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-location p {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 400;
                  font-size: 14px;
                }
                
                .container-body-email .container-reservation .container-reservation-info .container-location p b {
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-weight: 700;
                  font-size: 14px;
                }
                
                .container-body-email .container-footer-info {
                  margin-top: 2rem;
                  max-width: 695px;
                  color: #384860;
                }
                .container-reservation-warning {
                  width: 715px;
                  background-color: #FFFAF0;
                  padding: 10px 15px;
                  text-align: justify;
                  border-radius: 15px;
                  margin-top: 15px;
                  font-family: 'Product Sans', Arial, sans-serif;
                  font-size: 14px;
                }
                .icon-container {
                  display: inline-block;
                  color: #EFBD0C;
                  font-size: 20px;
                }
                .text-warning{
                  padding-left: 30px;
                  padding-right: 15px;
                }
                .link-warning{
                  color: black;
                }
                .container-reservation-payment{
                  width: 715px;
                  border-radius: 15px;
                  background-color: #F9F9F9;
                  padding: 15px 20px;
                }
                .align-right {
                  float: right;
                }
                .total-container {
                  border-top: 1px solid black;
                }
                """;
    }

}

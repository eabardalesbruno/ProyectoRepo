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
    private long payment;
    private Integer standbyHours;

    public ConfirmReserveBookingTemplateEmail(
            String monthInit,
            String monthEnd,
            String dayInit,
            String dayEnd,
            long dayInterval,
            String roomName,
            String clientName,
            String bookingId,
            String totalPeoples,
            long payment,
            Integer standbyHours) {
        this.monthInit = monthInit;
        this.monthEnd = monthEnd;
        this.dayInit = dayInit;
        this.dayEnd = dayEnd;
        this.dayInterval = dayInterval;
        this.roomName = roomName;
        this.clientName = clientName;
        this.bookingId = bookingId;
        this.totalPeoples = totalPeoples;
        this.payment = payment;
        this.standbyHours = standbyHours;
    }
    /*
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
                   <img src="https://dev.cieneguillariberadelrio.com/assets/images/svg/alarm/alarm-icon-green.svg"
                   alt ="icon-alarm"/>
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
                </strong></p>""";
        if (payment == 0) {
            body += "<div>" +
                    "<a href=\"https://www.cieneguillariberadelrio.com/payment-method/%bookingId\" class=\"button\">Pagar ahora</a>" +
                    "</div>";
        }
            body += "</div>" +
                    "<p class=\"font-size\"> " +
                    "Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a través de agente BCP, agencias o cualquier método de pago dentro de la plataforma usando este enlace: " +
                    "<a href=\"https://www.cieneguillariberadelrio.com/payment-method/%bookingId\">www.riberadelrio/reservas.com</a>" +
                    "</p>";
        return body.replaceAll("%roomName", roomName).replaceAll("%clientName", clientName)
                .replace("%monthInit", monthInit).replace("%monthEnd", monthEnd)
                .replace("%dayInit", dayInit).replace("%dayEnd", dayEnd)
                .replace("%dayInterval", String.valueOf(dayInterval))
                .replaceAll("%bookingId", String.valueOf(bookingId))
                .replace("%totalPeoples", String.valueOf(totalPeoples))
                .replace("%payment", String.valueOf(payment));
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
    */
    /*
    @Override
    public String execute() {
        String body = """
                <section class="container-body-email">
                  <h3 class="title">
                    ¡Gracias, %clientName! Tu reserva en Cieneguilla está en espera.
                  </h3>
                  <p class="description">
                    Hola %clientName, este correo es para recordarte de que mantiene un pago
                    pendiente de su reserva de:
                    <b class="description-bold">%roomName.</b>
                    Tienes plazo de realizar tu pago de %standbyHours horas. Pasando ese limite se anulara
                    tu reserva.
                  </p>

                  <table class="container-card" cellpadding="0" cellspacing="0" style="background: #fff; border: 1px solid #d9d9d9; border-radius: 16px; padding: 24px; max-width: 300px; margin: 0; text-align: center;">
                    <tbody>
                      <tr>
                        <td style="padding:10px;">
                          <img
                            src="%urlIcon"
                            alt="icon-calendar"
                            style="display: block; margin: 0 auto;"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td style="padding: 10px;">
                          <p style="margin: 0; font-family: Poppins, Arial, sans-serif; font-weight: 600; font-size: 16px; text-align: center; color: #1e1e1e;">Tienes %standbyHours horas para pagar</p>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding: 10px;">
                          <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId" style="background: #025928; border-radius: 24px; padding: 8px 16px; color: #fff; text-decoration: none; font-size: 14px; display: inline-block;">Pagar ahora</a>
                        </td>
                      </tr>
                    </tbody>
                  </table>

                  <div class="container-reservation">
                    <h5>Datos de la reserva</h5>
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
        return  body
                .replaceAll("%roomName", roomName)
                .replaceAll("%clientName", clientName)
                .replaceAll("%bookingId", String.valueOf(bookingId))
                .replaceAll("%standbyHours",String.valueOf(standbyHours))
                .replace("%monthInit", monthInit)
                .replace("%monthEnd", monthEnd)
                .replace("%dayInit", dayInit)
                .replace("%dayEnd", dayEnd)
                .replace("%dayInterval", String.valueOf(dayInterval))
                .replace("%totalPeoples", String.valueOf(totalPeoples))
                .replace("%urlIcon","https://s3.us-east-2.amazonaws.com/backoffice.documents/email/calendario.png");
    }
    */

    @Override
    public String execute() {
        String body = """
                <section class="container-body-email">
                  <h3 class="title">
                    ¡Gracias, %clientName! Tu reserva en Cieneguilla está en espera.
                  </h3>
                """;

        if (this.standbyHours != null && this.standbyHours > 0) {
            body += """
                  <p class="description">
                    Hola %clientName, este correo es para recordarte de que mantiene un pago
                    pendiente de su reserva de:
                    <b class="description-bold">%roomName.</b>
                    Tienes plazo de realizar tu pago de %standbyHours horas. Pasando ese limite se anulara
                    tu reserva.
                  </p>
                
                  <table class="container-card" cellpadding="0" cellspacing="0" style="background: #fff; border: 1px solid #d9d9d9; border-radius: 16px; padding: 24px; max-width: 300px; margin: 0; text-align: center;">
                    <tbody>
                      <tr>
                        <td style="padding:10px;">
                          <img
                            src="%urlIcon"
                            alt="icon-calendar"
                            style="display: block; margin: 0 auto;"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td style="padding: 10px;">
                          <p style="margin: 0; font-family: Poppins, Arial, sans-serif; font-weight: 600; font-size: 16px; text-align: center; color: #1e1e1e;">Tienes %standbyHours horas para pagar</p>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding: 10px;">
                          <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId" style="background: #025928; border-radius: 24px; padding: 8px 16px; color: #fff; text-decoration: none; font-size: 14px; display: inline-block;">Pagar ahora</a>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                """;
        } else {
            body += """
                  <p class="description">
                    Hola %clientName, tu reserva para <b class="description-bold">%roomName</b> se ha registrado.
                    Por favor, procede con el pago para confirmarla.
                  </p>
                  <table class="container-card" cellpadding="0" cellspacing="0" style="background: #fff; border: 1px solid #d9d9d9; border-radius: 16px; padding: 24px; max-width: 300px; margin: 0; text-align: center;">
                    <tbody>
                      <tr>
                        <td style="padding:10px;">
                          <img
                            src="%urlIcon"
                            alt="icon-calendar"
                            style="display: block; margin: 0 auto;"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td style="padding: 10px;">
                          <p style="margin: 0; font-family: Poppins, Arial, sans-serif; font-weight: 600; font-size: 16px; text-align: center; color: #1e1e1e;">¡Confirma tu reserva ahora!</p>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding: 10px;">
                          <a href="https://www.cieneguillariberadelrio.com/payment-method/%bookingId" style="background: #025928; border-radius: 24px; padding: 8px 16px; color: #fff; text-decoration: none; font-size: 14px; display: inline-block;">Pagar ahora</a>
                        </td>
                      </tr>
                    </tbody>
                  </table>
             """;
        }

        body += """
                  <div class="container-reservation">
                    <h5>Datos de la reserva</h5>
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

        // Reemplazos de variables
        String finalBody = body
                .replaceAll("%roomName", roomName)
                .replaceAll("%clientName", clientName)
                .replaceAll("%bookingId", String.valueOf(bookingId))
                .replace("%monthInit", monthInit)
                .replace("%monthEnd", monthEnd)
                .replace("%dayInit", dayInit)
                .replace("%dayEnd", dayEnd)
                .replace("%dayInterval", String.valueOf(dayInterval))
                .replace("%totalPeoples", String.valueOf(totalPeoples))
                .replace("%urlIcon","https://s3.us-east-2.amazonaws.com/backoffice.documents/email/calendario.png");

        if (this.standbyHours != null) {
            finalBody = finalBody.replaceAll("%standbyHours",String.valueOf(standbyHours));
        } else {
            finalBody = finalBody.replaceAll("%standbyHours","");
        }

        return finalBody;
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
                """;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }
}

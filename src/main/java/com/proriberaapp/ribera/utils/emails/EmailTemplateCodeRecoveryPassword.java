package com.proriberaapp.ribera.utils.emails;

public class EmailTemplateCodeRecoveryPassword implements EmailHandler {

    private EmailHandler nextHandler;
    private String code;

    private String clientName;

    public EmailTemplateCodeRecoveryPassword( String code, String clientName) {
        this.code = code;
        this.clientName = clientName;
    }

    @Override
    public void setNext(EmailHandler emailHandler) {
        this.nextHandler = emailHandler;
    }

    @Override
    public String execute() {
        // TODO Auto-generated method stub

        String body = """
                 <p class="font-size">Estimado(a) %clientName. Estás recibiendo este email porque se ha solicitado un cambio de contraseña para tu cuenta.
                 </p>
                 <p class="font-size">
                 <a href="http://localhost:4200/recovery-password?code=%code" class="button">Restablecer contraseña</a>
                  </p>
                   <p class="font-size">
                  Si no has solicitado un cambio de contraseña, puedes ignorar o eliminar este e-mail.
                 Saludos,
                 El equipo de Ribera del Rio Club Resort.
                 </p>
                """;
        return body.replace("%clientName", clientName).replace("%code", code);
    }

    @Override
    public String getStyles() {
        return "";
    }

}

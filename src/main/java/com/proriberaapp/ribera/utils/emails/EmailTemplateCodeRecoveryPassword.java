package com.proriberaapp.ribera.utils.emails;

public class EmailTemplateCodeRecoveryPassword implements EmailHandler {

    private EmailHandler nextHandler;
    private String code;

    private String clientName;
    private String baseUrl;

    public EmailTemplateCodeRecoveryPassword(String code, String clientName, String baseUrl) {
        this.code = code;
        this.clientName = clientName;
        this.baseUrl = baseUrl;
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
                 <a href="%baseUrl/recovery-password?code=%code" class="button">Restablecer contraseña</a>
                  </p>
                   <p class="font-size">
                  Si no has solicitado un cambio de contraseña, puedes ignorar o eliminar este e-mail.
                 Saludos,
                 El equipo de Ribera del Rio Club Resort.
                 </p>
                """;
        return body.replace("%clientName", clientName).replace("%code", code)
                .replace("%baseUrl", baseUrl);
    }

    @Override
    public String getStyles() {
        return "";
    }

}

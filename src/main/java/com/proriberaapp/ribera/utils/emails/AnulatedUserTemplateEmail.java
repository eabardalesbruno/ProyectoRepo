package com.proriberaapp.ribera.utils.emails;

public class AnulatedUserTemplateEmail implements EmailHandler {
    private EmailHandler nextHandler;
    private String clientName;
    private String roomName;

    public AnulatedUserTemplateEmail(String clientName,  String roomName) {
        this.clientName = clientName;
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
                Le informamos que su reserva para la habitación <strong>%roomName</strong> fue anulada.
                </p>
                """;
        return body.replaceAll("%clientName", clientName).replaceAll("%roomName",
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

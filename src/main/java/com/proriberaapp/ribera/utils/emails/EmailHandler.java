package com.proriberaapp.ribera.utils.emails;

public interface EmailHandler {
    void setNext(EmailHandler emailHandler);

    String execute();

    String getStyles();
}

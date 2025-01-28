package com.proriberaapp.ribera.Infraestructure.exception;

public class PasswordResetCodeExpiretTimeException extends RuntimeException {
    public PasswordResetCodeExpiretTimeException(String code) {
        super("El codigo " + code + " ha expirado");
    }
}

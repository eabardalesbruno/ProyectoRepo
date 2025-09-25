package com.proriberaapp.ribera.Infraestructure.exception;

public class PasswordResetCodeNotFoundException extends Exception {
    public PasswordResetCodeNotFoundException(String code) {
        super("El codigo " + code+ " no fue encontrado");
    }
    
}

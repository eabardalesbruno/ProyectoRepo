package com.proriberaapp.ribera.Infraestructure.exception;

public class PasswordResetCodeUsedException extends Exception {
    public PasswordResetCodeUsedException(String code) {
        super("El codigo " + code + " ya fue usado");
    }
    
}
